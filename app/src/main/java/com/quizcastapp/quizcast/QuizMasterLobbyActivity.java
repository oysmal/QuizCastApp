package com.quizcastapp.quizcast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.quizcastapp.context.QuizcastContext;

public class QuizMasterLobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_master_lobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView view = (TextView) findViewById(R.id.quiz_name_lobby_id);
        view.setText(QuizcastContext.getInstance(this).getQuiz().getName());
    }

    public void onClickStartGame(View v) {
        Intent intent = new Intent(this, QuizMasterGameActivity.class);
        startActivity(intent);
        finish();
    }

}
