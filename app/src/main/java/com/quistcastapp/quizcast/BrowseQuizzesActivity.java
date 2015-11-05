package com.quistcastapp.quizcast;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.quizcastapp.quizcast.context.QuizcastContext;

import org.json.JSONException;
import org.json.JSONObject;

public class BrowseQuizzesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_quizzes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void onClickBrowseByGenre(View v) {
        JSONObject j = new JSONObject();
        try {
            j.put("browsebygenre", "browse by genre");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        QuizcastContext.getInstance(this.getApplicationContext()).loadQuizzes();

    }

}
