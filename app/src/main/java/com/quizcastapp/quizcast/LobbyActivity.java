package com.quizcastapp.quizcast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.quizcastapp.context.QuizcastContext;

public class LobbyActivity extends AppCompatActivity {

    private QuizcastContext quizcastContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        quizcastContext = QuizcastContext.getInstance(this);


    }

}
