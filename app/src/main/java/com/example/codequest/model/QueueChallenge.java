package com.example.codequest.model;

public class QueueChallenge extends Challenge {

    public static final int ENQUEUE = 1;
    public static final int DEQUEUE = 2;

    private int queueAction;

    public QueueChallenge(String question, int queueAction) {
        super(question, "QUEUE");
        this.queueAction = queueAction;
    }

    public int getQueueAction() {
        return queueAction;
    }
}