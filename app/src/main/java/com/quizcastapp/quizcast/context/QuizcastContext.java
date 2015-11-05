package com.quizcastapp.quizcast.context;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.quistcastapp.quizcast.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by oysmal on 05.11.2015.
 */
public class QuizcastContext {

    private static QuizcastContext instance = null;
    private Context context = null;

    private JSONObject quiz = null;

    // Private constructor so we need to use getInstance
    private QuizcastContext(Context context) {
        this.context = context;
    }

    // Get singleton instance
    public static QuizcastContext getInstance(Context context) {
        if(instance == null) {
            instance = new QuizcastContext(context);
        }
        return instance;
    }

    public JSONObject getQuiz() {
        return quiz;
    }

    public void setQuiz(JSONObject quiz) {
        this.quiz = quiz;
    }

    public void loadQuizzes() {
        Log.d(context.getString(R.string.LOGTAG), "Fetching quizzes");
        LoadQuizzesTask.get("quiz/", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                JSONObject firstQuiz = null;
                try {
                    firstQuiz = (JSONObject) response.get(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(context.getString(R.string.LOGTAG), "Error parsing json");
                    return;
                }

                Log.d(context.getString(R.string.LOGTAG), firstQuiz.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(context.getString(R.string.LOGTAG), response.toString());
            }
        });

    }

    public void setActiveQuiz(String quizid) {

    }
}
