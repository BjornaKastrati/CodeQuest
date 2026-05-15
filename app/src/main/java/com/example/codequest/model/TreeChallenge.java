package com.example.codequest.model;

public class TreeChallenge extends Challenge {

    public static final int INORDER = 1;
    public static final int PREORDER = 2;
    public static final int POSTORDER = 3;
    public static final int PATH = 4;

    private int treeTaskType;
    private int[] correctSequence;
    private String hint;

    public TreeChallenge(String question, int treeTaskType, int[] correctSequence, String hint) {
        super(question, "TREE");
        this.treeTaskType = treeTaskType;
        this.correctSequence = correctSequence;
        this.hint = hint;
    }

    public int getTreeTaskType() {
        return treeTaskType;
    }

    public int[] getCorrectSequence() {
        return correctSequence;
    }

    public String getHint() {
        return hint;
    }
}