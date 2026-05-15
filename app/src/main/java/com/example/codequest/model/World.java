package com.example.codequest.model;

import java.util.ArrayList;

public class World {

    private int worldNumber;
    private String title;
    private String subtitle;
    private ArrayList<Level> levels;

    public World(int worldNumber, String title, String subtitle) {
        this.worldNumber = worldNumber;
        this.title = title;
        this.subtitle = subtitle;
        this.levels = new ArrayList<>();
    }

    public void addLevel(Level level) {
        levels.add(level);
    }

    public int getWorldNumber() {
        return worldNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }
}