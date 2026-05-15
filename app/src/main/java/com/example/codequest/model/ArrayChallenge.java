package com.example.codequest.model;

public class ArrayChallenge extends Challenge {

    private int[] arrayValues;
    private int scenarioType; // 1 = tap index, 2 = change index to 99, 3 = multiple choice
    private int targetIndex;
    private String[] options;
    private String correctAnswer;

    public ArrayChallenge(
            String question,
            int[] arrayValues,
            int scenarioType,
            int targetIndex,
            String[] options,
            String correctAnswer
    ) {
        super(question, "ARRAY");
        this.arrayValues = arrayValues;
        this.scenarioType = scenarioType;
        this.targetIndex = targetIndex;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public int[] getArrayValues() {
        return arrayValues;
    }

    public int getScenarioType() {
        return scenarioType;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}