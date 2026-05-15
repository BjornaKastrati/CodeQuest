package com.example.codequest.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ProgressManager {

    private static final String PREF_NAME = "CodeQuestProgress";

    private SharedPreferences prefs;

    public ProgressManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public int getTotalXP() {
        return prefs.getInt("totalXP", 0);
    }

    public void addXP(int xpToAdd) {
        int currentXP = getTotalXP();
        prefs.edit().putInt("totalXP", currentXP + xpToAdd).apply();
    }

    public int getUnlockedWorld() {
        return prefs.getInt("unlockedWorld", 1);
    }

    public void unlockWorld(int worldNumber) {
        int currentUnlocked = getUnlockedWorld();

        if (worldNumber > currentUnlocked) {
            prefs.edit().putInt("unlockedWorld", worldNumber).apply();
        }
    }

    public int getUnlockedLevel(int worldNumber) {
        return prefs.getInt("unlockedLevel_world" + worldNumber, 1);
    }

    public void unlockLevel(int worldNumber, int levelNumber) {
        String key = "unlockedLevel_world" + worldNumber;
        int currentUnlocked = getUnlockedLevel(worldNumber);

        if (levelNumber > currentUnlocked) {
            prefs.edit().putInt(key, levelNumber).apply();
        }
    }

    public int getStars(int worldNumber, int levelNumber) {
        return prefs.getInt("stars_world" + worldNumber + "_level" + levelNumber, 0);
    }

    public int saveStarsAndReturnNewXP(int worldNumber, int levelNumber, int stars) {
        String key = "stars_world" + worldNumber + "_level" + levelNumber;

        int previousStars = getStars(worldNumber, levelNumber);

        if (stars <= previousStars) {
            return 0;
        }

        int oldXP = previousStars * 25;
        int newXP = stars * 25;
        int difference = newXP - oldXP;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, stars);
        editor.putInt("totalXP", getTotalXP() + difference);
        editor.apply();

        return difference;
    }

    public void completeWorld(int worldNumber) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("completedWorld" + worldNumber, true);

        if (worldNumber < 3) {
            int nextWorld = worldNumber + 1;
            if (nextWorld > getUnlockedWorld()) {
                editor.putInt("unlockedWorld", nextWorld);
            }
        }

        editor.apply();
    }

    public boolean isWorldCompleted(int worldNumber) {
        return prefs.getBoolean("completedWorld" + worldNumber, false);
    }

    public boolean isSoundEnabled() {
        return prefs.getBoolean("soundEnabled", true);
    }

    public void setSoundEnabled(boolean enabled) {
        prefs.edit().putBoolean("soundEnabled", enabled).apply();
    }

    public boolean isAnimationEnabled() {
        return prefs.getBoolean("animationEnabled", true);
    }

    public void setAnimationEnabled(boolean enabled) {
        prefs.edit().putBoolean("animationEnabled", enabled).apply();
    }

    public void resetProgress() {
        prefs.edit().clear().apply();
    }
}