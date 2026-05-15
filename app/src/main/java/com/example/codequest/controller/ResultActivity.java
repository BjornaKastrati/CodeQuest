package com.example.codequest.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codequest.R;
import com.example.codequest.util.ProgressManager;

public class ResultActivity extends AppCompatActivity {

    private TextView txtResultTitle, txtScore, txtXPChip, txtStarsChip, txtLevelChip, txtUnlockMessage;
    private LinearLayout layoutStars;
    private Button btnContinue;

    private int worldNumber;
    private int levelNumber;
    private int score;
    private int total;
    private int stars;
    private int newlyEarnedXP;

    private ProgressManager progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        worldNumber = getIntent().getIntExtra("WORLD", 1);
        levelNumber = getIntent().getIntExtra("LEVEL", 1);
        score = getIntent().getIntExtra("SCORE", 0);
        total = getIntent().getIntExtra("TOTAL", 1);

        progressManager = new ProgressManager(this);

        connectViews();

        stars = score;
        newlyEarnedXP = saveProgress();

        showResult();

        btnContinue.setOnClickListener(v -> continueFlow());
    }

    private void connectViews() {
        txtResultTitle = findViewById(R.id.txtResultTitle);
        txtScore = findViewById(R.id.txtScore);
        layoutStars = findViewById(R.id.layoutStars);
        txtXPChip = findViewById(R.id.txtXPChip);
        txtStarsChip = findViewById(R.id.txtStarsChip);
        txtLevelChip = findViewById(R.id.txtLevelChip);
        txtUnlockMessage = findViewById(R.id.txtUnlockMessage);
        btnContinue = findViewById(R.id.btnContinue);
    }

    private void showResult() {
        txtResultTitle.setText("Level " + levelNumber + " Complete");
        txtScore.setText("First-try answers: " + score + "/" + total);

        buildStarRow();

        if (newlyEarnedXP > 0) {
            txtXPChip.setText("XP: +" + newlyEarnedXP);
        } else {
            txtXPChip.setText("XP: saved");
        }

        txtStarsChip.setText(score + " / " + total + " stars");
        txtLevelChip.setText("Level " + levelNumber + " complete");

        if (levelNumber == 5) {
            txtUnlockMessage.setText("World complete! Victory Selfie unlocked.");
        } else {
            txtUnlockMessage.setText("Next level unlocked!");
        }
    }

    private void buildStarRow() {
        layoutStars.removeAllViews();

        for (int i = 0; i < total; i++) {
            ImageView star = new ImageView(this);

            if (i < stars) {
                star.setImageResource(R.drawable.star_full);
            } else {
                star.setImageResource(R.drawable.star_empty);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(38),
                    dpToPx(38)
            );

            params.setMargins(dpToPx(4), 0, dpToPx(4), 0);
            star.setLayoutParams(params);

            layoutStars.addView(star);
        }
    }

    private int saveProgress() {
        int xpDifference = progressManager.saveStarsAndReturnNewXP(worldNumber, levelNumber, stars);

        if (levelNumber < 5) {
            progressManager.unlockLevel(worldNumber, levelNumber + 1);
        }

        if (levelNumber == 5) {
            progressManager.completeWorld(worldNumber);
        }

        return xpDifference;
    }

    private void continueFlow() {
        if (levelNumber == 5) {
            Intent intent = new Intent(this, VictorySelfieIntroActivity.class);
            intent.putExtra("WORLD", worldNumber);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LevelSelectionActivity.class);
            intent.putExtra("WORLD", worldNumber);
            startActivity(intent);
            finish();
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }
}