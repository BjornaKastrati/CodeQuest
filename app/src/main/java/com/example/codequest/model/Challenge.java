package com.example.codequest.model;

public class Challenge {

    private String question;
    private String type;

    public Challenge(String question, String type) {
        this.question = question;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public String getType() {
        return type;
    }
}