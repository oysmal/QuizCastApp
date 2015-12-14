package com.quizcastapp.quizcast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.quizcastapp.context.ChromecastClass;
import com.quizcastapp.context.QuizcastContext;
import com.quizcastapp.database.Question;
import com.quizcastapp.database.Quiz;
import com.quizcastapp.database.QuizCastMessageBuilder;

public class QuizMasterGameActivity extends AppCompatActivity {

    private QuizcastContext qc;
    private Quiz quiz;
    private int question_index;
    private ChromecastClass chromecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_master_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        qc = QuizcastContext.getInstance(this);
        quiz = qc.getQuiz();

        chromecast = ChromecastClass.getInstance(this);

        question_index = 0;
        TextView correctAnswer = (TextView) findViewById(R.id.text_view_correct_answer_quizmaster);
        correctAnswer.setText(quiz.getQuestions().get(question_index).getAnswers().get(0).getAnswer());
        question_index++;
    }

    public void sendQuestionToChromecast(Question q) {
        chromecast.sendMessageToChromecast(QuizCastMessageBuilder.
                generateQuizMasterQuestionMessage(quiz.getQuestions().get(question_index), question_index));  // plus one to show correctly on cast.
    }

    public void onClickNextQuestion(View view) {
        Log.d("QUIZCAST_LOG", String.valueOf(question_index));
        if(question_index == quiz.getQuestions().size()) {
            onClickFinish();
        } else {
            if(question_index == quiz.getQuestions().size()-1) {
                Button button = (Button) findViewById(R.id.button_next_question_quizmaster);
                button.setText(getString(R.string.finish_quiz_quizmaster_game));
            }
            TextView correctAnswer = (TextView) findViewById(R.id.text_view_correct_answer_quizmaster);
            correctAnswer.setText(quiz.getQuestions().get(question_index).getAnswers().get(0).getAnswer());

            sendQuestionToChromecast(quiz.getQuestions().get(question_index));
        }
        question_index++;

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

    public void onClickFinish() {
        chromecast.sendMessageToChromecast(QuizCastMessageBuilder.generateQuizMasterFinishMessage());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
