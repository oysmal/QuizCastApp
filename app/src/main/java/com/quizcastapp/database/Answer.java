package com.quizcastapp.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by oysmal on 06.11.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Answer {
    private String answer;
    private boolean correct;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
