package com.example.codequest.model;

public class MultipleChoiceChallenge extends Challenge {

    private String[] options;
    private String correctAnswer;

    public MultipleChoiceChallenge(String question, String[] options, String correctAnswer) {
        super(question, "MULTIPLE_CHOICE");
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}