package com.quizcastapp.quizcast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.quizcastapp.context.ChromecastClass;
import com.quizcastapp.context.QuizcastContext;
import com.quizcastapp.context.ResponseHandler;
import com.quizcastapp.database.Quiz;

import java.util.ArrayList;

public class BrowseQuizzes extends AppCompatActivity {

    private QuizcastContext quizcastContext;
    private ListView listView;
    private QuizAdapter quizAdapter;
    private ChromecastClass chromecast;

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

        chromecast = ChromecastClass.getInstance(this);

        quizcastContext.loadAllQuizzes(new ResponseHandler() {
            @Override
            public void onSuccess(ArrayList<Quiz> quizzes) {
                Log.d(getString(R.string.LOGTAG), "Got all quizzes length: " + quizzes.size());
                quizAdapter.addAll(quizzes);
            }

            @Override
            public void onError(String msg) {
                Log.d(getString(R.string.LOGTAG), msg);
            }
        });
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
