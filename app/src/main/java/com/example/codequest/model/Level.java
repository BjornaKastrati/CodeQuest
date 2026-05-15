package com.example.codequest.model;

import java.util.ArrayList;

public class Level {

    private int levelNumber;
    private String title;
    private String ruleText;
    private ArrayList<Challenge> challenges;

    public Level(int levelNumber, String title, String ruleText) {
        this.levelNumber = levelNumber;
        this.title = title;
        this.ruleText = ruleText;
        this.challenges = new ArrayList<>();
    }

    public void addChallenge(Challenge challenge) {
        challenges.add(challenge);
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getRuleText() {
        return ruleText;
    }

    public ArrayList<Challenge> getChallenges() {
        return challenges;
    }
}