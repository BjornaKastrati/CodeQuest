package com.example.codequest.model;

public class StackChallenge extends Challenge {

    public static final int PUSH = 1;
    public static final int POP = 2;

    private int stackAction;

    public StackChallenge(String question, int stackAction) {
        super(question, "STACK");
        this.stackAction = stackAction;
    }

    public int getStackAction() {
        return stackAction;
    }
}