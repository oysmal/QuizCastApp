package com.quizcastapp.quizcast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.quizcastapp.context.ChromecastClass;
import com.quizcastapp.context.QuizcastContext;
import com.quizcastapp.database.Quiz;
import com.quizcastapp.database.QuizCastMessageBuilder;

public class QuizMasterLobbyActivity extends AppCompatActivity {

    private ChromecastClass chromecast;
    private Quiz quiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_master_lobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView view = (TextView) findViewById(R.id.quiz_name_lobby_id);
        view.setText(QuizcastContext.getInstance(this).getQuiz().getName());

        chromecast = ChromecastClass.getInstance(this);
        quiz = QuizcastContext.getInstance(this).getQuiz();
        chromecast.sendMessageToChromecast(QuizCastMessageBuilder.generateQuizMasterInitMessage(
                quiz, "quizmaster"));
    }

    public void onClickStartGame(View v) {
        chromecast.sendMessageToChromecast(
                QuizCastMessageBuilder.generateQuizMasterQuestionMessage(
                        quiz.getQuestions().get(0), 0));
        Intent intent = new Intent(this, QuizMasterGameActivity.class);
        startActivity(intent);
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
}
