package com.quizcastapp.quizcast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.quizcastapp.context.QuizcastContext;
import com.quizcastapp.context.ResponseHandler;
import com.quizcastapp.database.Quiz;

import java.util.ArrayList;

public class BrowseQuizzes extends AppCompatActivity {

    private QuizcastContext quizcastContext;
    private ListView listView;
    private QuizAdapter quizAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_quizzes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        quizcastContext = QuizcastContext.getInstance(this);
        listView = (ListView) findViewById(R.id.search_list);
        quizAdapter = new QuizAdapter(this, R.layout.item_quiz, new ArrayList<Quiz>());
        listView.setAdapter(quizAdapter);

        quizcastContext.loadAllQuizzes(new ResponseHandler() {
            @Override
            public void onSuccess(ArrayList<Quiz> quizzes) {
                Log.d(getString(R.string.LOGTAG), "Got all quizzes lenght: " + quizzes.size());
                quizAdapter.addAll(quizzes);
            }

            @Override
            public void onError(String msg) {
                Log.d(getString(R.string.LOGTAG), msg);
            }
        });
    }

}
