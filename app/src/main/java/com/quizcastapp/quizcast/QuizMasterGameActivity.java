package com.quizcastapp.quizcast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizcastapp.context.ChromecastClass;
import com.quizcastapp.context.QuizcastContext;
import com.quizcastapp.database.Question;
import com.quizcastapp.database.Quiz;

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
        question_index = 0;


        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(quiz.getQuestions().get(question_index));
            chromecast = ChromecastClass.getInstance(this, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        TextView correctAnswer = (TextView) findViewById(R.id.text_view_correct_answer_quizmaster);
        correctAnswer.setText(quiz.getQuestions().get(question_index).getAnswers().get(0).getAnswer());
    }

    public void sendQuestionToChromecast(Question q) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(quiz.getQuestions().get(question_index));
            Log.d(getString(R.string.LOGTAG), json);
            //chromecast.sendMessageToChromecast(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void onClickNextQuestion(View view) {
        question_index++;
        if(question_index == quiz.getQuestions().size()) {
            this.finish();
        } else {
            if(question_index+1 == quiz.getQuestions().size()) {
                Button button = (Button) findViewById(R.id.button_next_question_quizmaster);
                button.setText(getString(R.string.finish_quiz_quizmaster_game));
            }

            TextView correctAnswer = (TextView) findViewById(R.id.text_view_correct_answer_quizmaster);
            correctAnswer.setText(quiz.getQuestions().get(question_index).getAnswers().get(0).getAnswer());

            sendQuestionToChromecast(quiz.getQuestions().get(question_index));
        }
    }

}
