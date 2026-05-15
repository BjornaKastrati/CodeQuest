package com.example.codequest.model;

public class BossLogicChallenge extends Challenge {

    private boolean inputA;
    private boolean inputB;
    private boolean inputC;
    private boolean correctOutput;

    private String firstGate;
    private String secondGate;
    private boolean usesSecondGate;

    public BossLogicChallenge(String question, boolean inputA, boolean inputB, boolean inputC,
                              boolean correctOutput, String firstGate,
                              String secondGate, boolean usesSecondGate) {
        super(question, "BOSS_LOGIC");

        this.inputA = inputA;
        this.inputB = inputB;
        this.inputC = inputC;
        this.correctOutput = correctOutput;
        this.firstGate = firstGate;
        this.secondGate = secondGate;
        this.usesSecondGate = usesSecondGate;
    }

    public boolean isInputA() {
        return inputA;
    }

    public boolean isInputB() {
        return inputB;
    }

    public boolean isInputC() {
        return inputC;
    }

    public boolean isCorrectOutput() {
        return correctOutput;
    }

    public String getFirstGate() {
        return firstGate;
    }

    public String getSecondGate() {
        return secondGate;
    }

    public boolean usesSecondGate() {
        return usesSecondGate;
    }
}