package com.quizcastapp.context;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.quizcastapp.database.Quiz;
import com.quizcastapp.quizcast.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by oysmal on 05.11.2015.
 */
public class QuizcastContext {

    private static QuizcastContext instance = null;
    private Context context;

    private Quiz quiz;
    private ArrayList<Quiz> quizzes;
    private String nickname;

    // Private constructor so we need to use getInstance
    private QuizcastContext(Context context) {
        this.context = context;
        this.quizzes = null;
        this.quiz = null;
    }

    // Get singleton instance
    public static QuizcastContext getInstance(Context context) {
        if(instance == null) {
            instance = new QuizcastContext(context);
        }
        return instance;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public ArrayList<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(ArrayList<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public void loadQuizzes(final ResponseHandler rHandler) {
        Log.d(context.getString(R.string.LOGTAG), "Fetching all quizzes");
        final QuizcastContext self = this;
        LoadQuizzesTask.get("quiz/", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    for(int i = 0; i < response.length(); i++) {
                        quizzes.add(
                                mapper.readValue(response.get(i).toString(), Quiz.class)
                        );
                    }

                    self.quizzes = quizzes;
                    rHandler.onSuccess(quizzes);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(context.getString(R.string.LOGTAG), "Error parsing json");
                    rHandler.onError(e.getMessage());
                } catch (JsonMappingException e) {
                    Log.d(context.getString(R.string.LOGTAG), e.getMessage());
                    e.printStackTrace();
                    rHandler.onError(e.getMessage());
                } catch (JsonParseException e) {
                    Log.d(context.getString(R.string.LOGTAG), e.getMessage());
                    e.printStackTrace();
                    rHandler.onError(e.getMessage());
                } catch (IOException e) {
                    Log.d(context.getString(R.string.LOGTAG), e.getMessage());
                    e.printStackTrace();
                    rHandler.onError(e.getMessage());
                }
            }
        });

    }


    public void loadQuizzesByName(String name, final ResponseHandler rHandler) {
        Log.d(context.getString(R.string.LOGTAG), "Fetching quizzes by name: " + name);

        LoadQuizzesTask.get("quiz/searchByName/" + name, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        quizzes.add(
                                mapper.readValue(response.get(i).toString(), Quiz.class)
                        );
                    }

                    // Successfully got the quizzes
                    rHandler.onSuccess(quizzes);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(context.getString(R.string.LOGTAG), e.getMessage());
                    rHandler.onError(e.getMessage());
                } catch (JsonMappingException e) {
                    Log.e(context.getString(R.string.LOGTAG), e.getMessage());
                    e.printStackTrace();
                    rHandler.onError(e.getMessage());
                } catch (JsonParseException e) {
                    Log.e(context.getString(R.string.LOGTAG), e.getMessage());
                    e.printStackTrace();
                    rHandler.onError(e.getMessage());
                } catch (IOException e) {
                    Log.e(context.getString(R.string.LOGTAG), e.getMessage());
                    e.printStackTrace();
                    rHandler.onError(e.getMessage());
                }

            }

        });
    }

    public void loadAllQuizzes(final ResponseHandler rHandler) {
        LoadQuizzesTask.get("quiz", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Quiz> quizzes = new ArrayList<>();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        quizzes.add(
                                mapper.readValue(response.get(i).toString(), Quiz.class)
                        );
                    }

                    // Successfully got the quizzes
                    rHandler.onSuccess(quizzes);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(context.getString(R.string.LOGTAG), e.getMessage());
                    rHandler.onError(e.getMessage());
                } catch (JsonMappingException e) {
                    Log.e(context.getString(R.string.LOGTAG), e.getMessage());
                    e.printStackTrace();
                    rHandler.onError(e.getMessage());
                } catch (JsonParseException e) {
                    Log.e(context.getString(R.string.LOGTAG), e.getMessage());
                    e.printStackTrace();
                    rHandler.onError(e.getMessage());
                } catch (IOException e) {
                    Log.e(context.getString(R.string.LOGTAG), e.getMessage());
                    e.printStackTrace();
                    rHandler.onError(e.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(context.getString(R.string.LOGTAG), errorResponse.toString());
                rHandler.onError(errorResponse.toString());
            }
        });
    }

    public void setActiveQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
