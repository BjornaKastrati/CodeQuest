package com.example.codequest.model;

public class CodeOrderChallenge extends Challenge {

    private String[] codeBlocks;
    private int[] correctOrder;

    public CodeOrderChallenge(String question, String[] codeBlocks, int[] correctOrder) {
        super(question, "CODE_ORDER");
        this.codeBlocks = codeBlocks;
        this.correctOrder = correctOrder;
    }

    public String[] getCodeBlocks() {
        return codeBlocks;
    }

    public int[] getCorrectOrder() {
        return correctOrder;
    }
}