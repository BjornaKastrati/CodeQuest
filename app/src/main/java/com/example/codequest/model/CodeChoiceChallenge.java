package com.example.codequest.model;

public class CodeChoiceChallenge extends Challenge {

    private String firstCode;
    private String secondCode;
    private int correctChoice; // 1 or 2

    public CodeChoiceChallenge(String question, String firstCode, String secondCode, int correctChoice) {
        super(question, "CODE_CHOICE");
        this.firstCode = firstCode;
        this.secondCode = secondCode;
        this.correctChoice = correctChoice;
    }

    public String getFirstCode() {
        return firstCode;
    }

    public String getSecondCode() {
        return secondCode;
    }

    public int getCorrectChoice() {
        return correctChoice;
    }
}