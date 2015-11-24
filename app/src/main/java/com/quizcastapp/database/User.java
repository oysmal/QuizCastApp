package com.quizcastapp.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by oysmal on 23/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
