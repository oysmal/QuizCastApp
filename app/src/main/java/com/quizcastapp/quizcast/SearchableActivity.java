package com.quizcastapp.quizcast;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.quizcastapp.context.ChromecastClass;
import com.quizcastapp.context.QuizcastContext;
import com.quizcastapp.context.ResponseHandler;
import com.quizcastapp.database.Quiz;

import java.util.ArrayList;


public class SearchableActivity extends AppCompatActivity {

    private QuizAdapter listAdapter;
    private ListView listView;
    private QuizcastContext quizcastContext;
    private ChromecastClass chromecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.search_list);
        listAdapter = new QuizAdapter(this, R.layout.item_quiz, new ArrayList<Quiz>());
        listView.setAdapter(listAdapter);

        this.quizcastContext = QuizcastContext.getInstance(this);
        chromecast = ChromecastClass.getInstance(this);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            this.quizcastContext.loadQuizzesByName(query, new ResponseHandler() {
                @Override
                public void onSuccess(ArrayList<Quiz> quizzes) {
                    for (Quiz item : quizzes) {
                        listAdapter.add(item);
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.d(getString(R.string.LOGTAG), msg);
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
