package com.quizcastapp.database;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oysmal on 25/11/15.
 */
public class QuizCastMessageBuilder {

    public static String generateQuizMasterQuestionMessage(Question q, int index) {

        try {
            JSONObject json = new JSONObject("{" +
                    "\"typeOfMessage\": \"question\", " +
                    "\"questionNumber\": " + index + "," +
                    "\"question\": \"" + q.getQuestion() + "\"," +
                    "\"answers\": [" +
                        "\"" + q.getAnswers().get(0).getAnswer() + "\", " +
                        "\"" + q.getAnswers().get(1).getAnswer() + "\", " +
                        "\"" + q.getAnswers().get(2).getAnswer() + "\", " +
                        "\"" + q.getAnswers().get(3).getAnswer() + "\"] " +
                    "}");
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateQuizMasterInitMessage(Quiz q, String mode) {
        try {
            JSONObject json = new JSONObject("{" +
                    "'typeOfMessage': 'info', " +
                    "'name': '" + q.getName() + "'," +
                    "'genre': '" + q.getGenre() + "'," +
                    "'mode': '" + mode + "'," +
                    "'numberOfQuestions': " + q.getQuestions().size() + "," +
                    "'answers': true" +
                    "}");
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateQuizMasterFinishMessage() {
        try {
            JSONObject json = new JSONObject("{" +
                    "'typeOfMessage': 'quizmasterdone'" +
                    "}");
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateQuizMasterCreateGameMessage() {
        try {
            JSONObject json = new JSONObject("{" +
                    "'typeOfMessage': 'creategame'" +
                    "}");
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
