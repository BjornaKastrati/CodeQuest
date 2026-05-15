package com.example.codequest.model;

public class LogicChallenge extends Challenge {

    private boolean inputA;
    private boolean inputB;
    private boolean correctOutput;
    private String gateType;

    public LogicChallenge(String question, boolean inputA, boolean inputB,
                          boolean correctOutput, String gateType) {
        super(question, "LOGIC");
        this.inputA = inputA;
        this.inputB = inputB;
        this.correctOutput = correctOutput;
        this.gateType = gateType;
    }

    public boolean isInputA() {
        return inputA;
    }

    public boolean isInputB() {
        return inputB;
    }

    public boolean isCorrectOutput() {
        return correctOutput;
    }

    public String getGateType() {
        return gateType;
    }
}