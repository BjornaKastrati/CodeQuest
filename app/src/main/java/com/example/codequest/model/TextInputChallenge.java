package com.example.codequest.model;

public class TextInputChallenge extends Challenge {

    private String correctAnswer;

    public TextInputChallenge(String question, String correctAnswer) {
        super(question, "TEXT_INPUT");
        this.correctAnswer = correctAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}