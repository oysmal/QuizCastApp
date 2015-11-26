package com.quizcastapp.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by oysmal on 25/11/15.
 */
public class QuizCastMessageBuilder {

    public static String generateQuizMasterQuestionMessage(Question q, int index) {

        String result = "{" +
                "typeOfMessage: question, " +
                "questionNumber: " + index +
                ", question: " + q.getQuestion() +
                ", answers: [" +
                         q.getAnswers().get(0).getAnswer() + ", " +
                         q.getAnswers().get(1).getAnswer() + ", " +
                         q.getAnswers().get(2).getAnswer() + ", " +
                         q.getAnswers().get(3).getAnswer() + ", " +
                "}";
        return result;
    }

    public static String generateQuizMasterInitMessage(Quiz q, String mode) {
        String result = "{" +
                "typeOfMessage: info, " +
                "name: " + q.getName() +
                ", genre: " + q.getGenre() +
                ", mode:  " + mode +
                ", numberOfQuestions:  " + q.getQuestions().size() +
                ", answers: true" +
                "}";
        return result;
    }

    public static String generateQuizMasterFinishMessage() {
        String result = "{" +
                "typeOfMessage: quizmasterdone" +
                "}";

        return result;
    }
}
