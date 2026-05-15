package com.example.codequest.model;

public class BinaryChallenge extends Challenge {

    private boolean[] bulbs;
    private String[] options;
    private String correctAnswer;

    public BinaryChallenge(String question, boolean[] bulbs, String[] options, String correctAnswer) {
        super(question, "BINARY");
        this.bulbs = bulbs;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public boolean[] getBulbs() {
        return bulbs;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}