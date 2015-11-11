package com.quizcastapp.context;

import android.util.Log;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;

class QuizCastChannel implements Cast.MessageReceivedCallback {
    public String getNamespace() {
        return "urn:x-cast:super.awesome.example";
    }

    @Override
    public void onMessageReceived(CastDevice castDevice, String namespace,
                                  String message) {
        Log.d("MYLOG", "onMessageReceived: " + message);
    }
}