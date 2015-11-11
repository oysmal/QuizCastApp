package com.quizcastapp.context;

import com.quizcastapp.database.Quiz;

import java.util.ArrayList;

/**
 * Created by oysmal on 06/11/15.
 */
public interface ResponseHandler {

    public void onSuccess(ArrayList<Quiz> quizzes);
    public void onError(String msg);
}
