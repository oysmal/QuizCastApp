package com.quizcastapp.context;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.quizcastapp.quizcast.R;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
/**
 * Created by oysmal on 08.03.15.
 */
public class ChromecastClass {

    MediaRouter mMediaRouter;
    MediaRouteSelector mMediaRouteSelector;
    CastDevice mSelectedDevice;
    MyMediaRouterCallback mMediaRouterCallback;
    Cast.Listener mCastClientListener;
    QuizCastChannel mQuizCastChannel;
    boolean mApplicationStarted = false;
    boolean mWaitingForReconnect = false;
    GoogleApiClient mApiClient;
    GoogleApiClient.ConnectionCallbacks mConnectionCallbacks;
    ConnectionFailedListener mConnectionFailedListener;
    String mSessionId;

    Context context;

    private static ChromecastClass instance = null;

    private static String defaultJsonMessage = null;


    protected ChromecastClass(Context context) {
        this.context = context;

        mMediaRouter = MediaRouter.getInstance(this.context);
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(context.getString(R.string.application_id_cast)))
                .build();
        Log.d(context.getString(R.string.LOGTAG), "mMediaRouteSelector: " + mMediaRouteSelector.toString());
        mMediaRouterCallback = new MyMediaRouterCallback();
    }

    public static boolean isInstanceNull() {
        return instance == null;
    }

    public static ChromecastClass getInstance(Context context) {
        if(instance == null) {
            Log.d(context.getString(R.string.LOGTAG), "Return new Chromecast instance");
            instance = new ChromecastClass(context);
            defaultJsonMessage = "";
        }
        return instance;
    }

    public void onResume() {
        // Start media router discovery
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }

    public void onPauseIsFinishing() {
        mMediaRouter.removeCallback(mMediaRouterCallback);
    }

    public void onDestroy() {
        teardown();
    }


    public boolean onCreateOptionsMenu(Menu inflatedMenu) {

        MenuItem mediaRouteMenuItem =  inflatedMenu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat
                .getActionProvider(mediaRouteMenuItem);
        // Set the MediaRouteActionProvider selector for device discovery.
        mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);

        return true;
    }


    public void sendMessageToChromecast(String json) {
        Log.d(context.getString(R.string.LOGTAG), json);
        sendMessage(json);
    }

    /**
     * Start the receiver app
     */
    public void launchReceiver() {
        try {
            mCastClientListener = new Cast.Listener() {

                @Override
                public void onApplicationDisconnected(int errorCode) {
                    Log.d(context.getString(R.string.LOGTAG), "application has stopped");
                    teardown();
                }

            };
            // Connect to Google Play services
            mConnectionCallbacks = new ConnectionCallbacks();
            mConnectionFailedListener = new ConnectionFailedListener();
            Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
                    .builder(mSelectedDevice, mCastClientListener);
            mApiClient = new GoogleApiClient.Builder(this.context)
                    .addApi(Cast.API, apiOptionsBuilder.build())
                    .addConnectionCallbacks(mConnectionCallbacks)
                    .addOnConnectionFailedListener(mConnectionFailedListener)
                    .build();

            mApiClient.connect();
        } catch (Exception e) {
            Log.e(context.getString(R.string.LOGTAG), "Failed launchReceiver", e);
        }
    }

    public void setUpGoogleApi() {
        mCastClientListener = new Cast.Listener() {
            @Override
            public void onApplicationStatusChanged() {
                if (mApiClient != null) {
                    Log.d(context.getString(R.string.LOGTAG), "onApplicationStatusChanged: "
                            + Cast.CastApi.getApplicationStatus(mApiClient));
                }
            }

            @Override
            public void onVolumeChanged() {
                if (mApiClient != null) {
                    Log.d(context.getString(R.string.LOGTAG), "onVolumeChanged: " + Cast.CastApi.getVolume(mApiClient));
                }
            }

            @Override
            public void onApplicationDisconnected(int errorCode) {
                teardown();
            }
        };
        mConnectionCallbacks = new ConnectionCallbacks();
        mConnectionFailedListener = new ConnectionFailedListener();

        Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
                .builder(mSelectedDevice, mCastClientListener);

        mApiClient = new GoogleApiClient.Builder(this.context)
                .addApi(Cast.API, apiOptionsBuilder.build())
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mConnectionFailedListener)
                .build();
        mApiClient.connect();
    }

    private class MyMediaRouterCallback extends MediaRouter.Callback {

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            if(mSelectedDevice == null) {
                mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            }
            String routeId = info.getId();

            setUpGoogleApi();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            teardown();
            mSelectedDevice = null;
        }
    }


    /**
     * Google Play services callbacks
     */
    public class ConnectionCallbacks implements
            GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle connectionHint) {
            Log.d(context.getString(R.string.LOGTAG), "onConnected");

            if (mApiClient == null) {
                // We got disconnected while this runnable was pending
                // execution.
                return;
            }

            try {
                if (mWaitingForReconnect) {
                    mWaitingForReconnect = false;

                    // Check if the receiver app is still running
                    if ((connectionHint != null)
                            && connectionHint
                            .getBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING)) {
                        Log.d(context.getString(R.string.LOGTAG), "App is no longer running");
                        teardown();
                    } else {
                        // Re-create the custom message channel
                        try {
                            Cast.CastApi.setMessageReceivedCallbacks(
                                    mApiClient,
                                    mQuizCastChannel.getNamespace(),
                                    mQuizCastChannel);
                        } catch (IOException e) {
                            Log.e(context.getString(R.string.LOGTAG), "Exception while creating channel", e);
                        }
                    }
                } else {
                    // Launch the receiver app
                    Cast.CastApi
                            .launchApplication(mApiClient,
                                    context.getString(R.string.application_id_cast), false)
                            .setResultCallback(
                                    new ResultCallback<Cast.ApplicationConnectionResult>() {
                                        @Override
                                        public void onResult(
                                                Cast.ApplicationConnectionResult result) {
                                            Status status = result.getStatus();
                                            Log.d(context.getString(R.string.LOGTAG),
                                                    "ApplicationConnectionResultCallback.onResult: statusCode "
                                                            + status.getStatusCode());
                                            if (status.isSuccess()) {
                                                ApplicationMetadata applicationMetadata = result
                                                        .getApplicationMetadata();
                                                mSessionId = result
                                                        .getSessionId();
                                                String applicationStatus = result
                                                        .getApplicationStatus();
                                                boolean wasLaunched = result
                                                        .getWasLaunched();
                                                Log.d(context.getString(R.string.LOGTAG),
                                                        "application name: "
                                                                + applicationMetadata
                                                                .getName()
                                                                + ", status: "
                                                                + applicationStatus
                                                                + ", sessionId: "
                                                                + mSessionId
                                                                + ", wasLaunched: "
                                                                + wasLaunched);
                                                mApplicationStarted = true;

                                                // Create the custom message
                                                // channel
                                                mQuizCastChannel = new QuizCastChannel();
                                                try {
                                                    Cast.CastApi
                                                            .setMessageReceivedCallbacks(
                                                                    mApiClient,
                                                                    mQuizCastChannel
                                                                            .getNamespace(),
                                                                    mQuizCastChannel);
                                                } catch (IOException e) {
                                                    Log.e(context.getString(R.string.LOGTAG),
                                                            "Exception while creating channel",
                                                            e);
                                                }

                                                // set the initial instructions
                                                // on the receiver
                                                //...
                                               // sendMessageToChromecast(defaultJsonMessage);
                                            } else {
                                                Log.e(context.getString(R.string.LOGTAG),
                                                        "application could not launch");
                                                teardown();
                                            }
                                        }
                                    });
                }
            } catch (Exception e) {
                Log.e(context.getString(R.string.LOGTAG), "Failed to launch application", e);
            }
        }

        @Override
        public void onConnectionSuspended(int cause) {
            Log.d(context.getString(R.string.LOGTAG), "onConnectionSuspended");
            mWaitingForReconnect = true;
        }
    }


    /**
     * Google Play services callbacks
     */
    public class ConnectionFailedListener implements
            GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            Log.e(context.getString(R.string.LOGTAG), "onConnectionFailed ");

            teardown();
        }
    }


    /**
     * Send a text message to the receiver
     *
     * @param message
     */
    private void sendMessage(String message) {
        if (mApiClient != null && mQuizCastChannel != null) {
            try {
                Cast.CastApi.sendMessage(mApiClient,
                        mQuizCastChannel.getNamespace(), message)
                        .setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status result) {
                                if (!result.isSuccess()) {
                                    Log.e(context.getString(R.string.LOGTAG), "Sending message failed");
                                }
                            }
                        });
            } catch (Exception e) {
                Log.e(context.getString(R.string.LOGTAG), "Exception while sending message", e);
            }
        } else {
            Log.d(context.getString(R.string.LOGTAG), "Sent message successfully");
            Log.d(context.getString(R.string.LOGTAG), message);
        }
    }

    public void teardown() {
        if( mApiClient != null ) {
            if( mApplicationStarted ) {
                try {
                    Cast.CastApi.stopApplication( mApiClient );
                    if( mQuizCastChannel != null ) {
                        Cast.CastApi.removeMessageReceivedCallbacks( mApiClient, mQuizCastChannel.getNamespace() );
                        mQuizCastChannel = null;
                    }
                } catch( IOException e ) {
                    Log.e(context.getString(R.string.LOGTAG), "Exception while removing application " + e );
                }
                mApplicationStarted = false;
            }
            if( mApiClient.isConnected() )
                mApiClient.disconnect();
            mApiClient = null;
        }
        mSelectedDevice = null;
    }


}
