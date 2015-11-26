package com.quizcastapp.quizcast;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quizcastapp.context.ChromecastClass;
import com.quizcastapp.context.QuizcastContext;
import com.quizcastapp.database.User;

import java.util.ArrayList;

public class LobbyActivity extends AppCompatActivity {

    private QuizcastContext quizcastContext;
    private ArrayList<User> joinedUsers;
    private ChromecastClass chromecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        chromecast.getInstance(this);

        quizcastContext = QuizcastContext.getInstance(this);


    }

    public void fetchConnectedUsers() {

    }

    public void onStartGame(View v) {
        //TODO: Start game in QuizastContext.
        Log.d(getString(R.string.LOGTAG), "Started game");
    }


    /**
     * Adapter for the list of joined users.
     */
    private class UserAdapter extends ArrayAdapter<User> {

        private ArrayList<User> data;
        private Context context;
        private int layoutResId;

        public UserAdapter(Context context, int layoutResId, ArrayList<User> objects) {
            super(context, layoutResId, objects);
            this.context = context;
            this.data = objects;
            this.layoutResId = layoutResId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            User user = getItem(position);
            View view = convertView;
            UserHolder holder = null;

            Log.d(context.getString(R.string.LOGTAG), "get view");

            if(view == null) {
                LayoutInflater inflater = ((Activity)this.context).getLayoutInflater();
                view = inflater.inflate(this.layoutResId, parent, false);

                holder = new UserHolder();
                holder.nameView = (TextView) view.findViewById(R.id.item_user_id);
                view.setTag(holder);

            } else {
                holder = (UserHolder) view.getTag();
            }

            holder.nameView.setText(user.getNickname());

            final int final_pos = position;

            return view;
        }

    }

    public static class UserHolder {
        public TextView nameView;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        chromecast.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start media router discovery
        chromecast.onResume();
    }

    @Override
    protected void onPause() {
        if (isFinishing()) {
            chromecast.onPauseIsFinishing();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        chromecast.onDestroy();
        super.onDestroy();
    }

}

