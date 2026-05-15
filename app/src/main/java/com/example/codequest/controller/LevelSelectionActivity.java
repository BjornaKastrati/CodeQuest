package com.example.codequest.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codequest.R;
import com.example.codequest.model.GameRepository;
import com.example.codequest.model.Level;
import com.example.codequest.model.World;
import com.example.codequest.util.ProgressManager;

public class LevelSelectionActivity extends AppCompatActivity {

    private int worldNumber;
    private int unlockedLevel;

    private TextView txtWorldTitle, txtWorldSubtitle;
    private LinearLayout cardLevel1, cardLevel2, cardLevel3, cardLevel4, cardLevel5;
    private TextView txtLevel1, txtLevel2, txtLevel3, txtLevel4, txtLevel5;
    private LinearLayout starsLevel1, starsLevel2, starsLevel3, starsLevel4, starsLevel5;

    private ProgressManager progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        worldNumber = getIntent().getIntExtra("WORLD", 1);

        progressManager = new ProgressManager(this);
        unlockedLevel = progressManager.getUnlockedLevel(worldNumber);

        connectViews();

        setupWorldHeader();
        setupLevelCards();
    }

    private void connectViews() {
        txtWorldTitle = findViewById(R.id.txtWorldTitle);
        txtWorldSubtitle = findViewById(R.id.txtWorldSubtitle);

        cardLevel1 = findViewById(R.id.cardLevel1);
        cardLevel2 = findViewById(R.id.cardLevel2);
        cardLevel3 = findViewById(R.id.cardLevel3);
        cardLevel4 = findViewById(R.id.cardLevel4);
        cardLevel5 = findViewById(R.id.cardLevel5);

        txtLevel1 = findViewById(R.id.txtLevel1);
        txtLevel2 = findViewById(R.id.txtLevel2);
        txtLevel3 = findViewById(R.id.txtLevel3);
        txtLevel4 = findViewById(R.id.txtLevel4);
        txtLevel5 = findViewById(R.id.txtLevel5);

        starsLevel1 = findViewById(R.id.starsLevel1);
        starsLevel2 = findViewById(R.id.starsLevel2);
        starsLevel3 = findViewById(R.id.starsLevel3);
        starsLevel4 = findViewById(R.id.starsLevel4);
        starsLevel5 = findViewById(R.id.starsLevel5);
    }

    private void setupWorldHeader() {
        World world = GameRepository.getWorld(worldNumber);

        txtWorldTitle.setText("World " + worldNumber + ": " + world.getTitle());
        txtWorldSubtitle.setText(world.getSubtitle());
    }

    private void setupLevelCards() {
        setupSingleLevelCard(cardLevel1, txtLevel1, starsLevel1, 1);
        setupSingleLevelCard(cardLevel2, txtLevel2, starsLevel2, 2);
        setupSingleLevelCard(cardLevel3, txtLevel3, starsLevel3, 3);
        setupSingleLevelCard(cardLevel4, txtLevel4, starsLevel4, 4);
        setupSingleLevelCard(cardLevel5, txtLevel5, starsLevel5, 5);
    }

    private void setupSingleLevelCard(
            LinearLayout card,
            TextView title,
            LinearLayout starRow,
            int levelNumber
    ) {
        Level level = GameRepository.getLevel(worldNumber, levelNumber);
        int earnedStars = progressManager.getStars(worldNumber, levelNumber);
        int maxStars = level.getChallenges().size();

        if (levelNumber <= unlockedLevel) {
            card.setAlpha(1f);
            card.setBackgroundResource(R.drawable.card_unlocked);
            title.setText("Level " + levelNumber + ": " + level.getTitle());

            buildStarRow(starRow, earnedStars, maxStars);

            card.setOnClickListener(v -> openChallenge(levelNumber));

        } else {
            card.setAlpha(0.55f);
            card.setBackgroundResource(R.drawable.card_locked);
            title.setText("Level " + levelNumber + ": " + level.getTitle() + "  LOCKED");

            buildStarRow(starRow, 0, maxStars);

            card.setOnClickListener(v ->
                    Toast.makeText(this, "Complete the previous level first!", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void buildStarRow(LinearLayout starRow, int earnedStars, int maxStars) {
        starRow.removeAllViews();

        for (int i = 0; i < maxStars; i++) {
            ImageView star = new ImageView(this);

            if (i < earnedStars) {
                star.setImageResource(R.drawable.star_full);
            } else {
                star.setImageResource(R.drawable.star_empty);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(24),
                    dpToPx(24)
            );

            params.setMargins(0, 0, dpToPx(6), 0);
            star.setLayoutParams(params);

            starRow.addView(star);
        }
    }

    private void openChallenge(int levelNumber) {
        Intent intent = new Intent(this, ChallengeActivity.class);
        intent.putExtra("WORLD", worldNumber);
        intent.putExtra("LEVEL", levelNumber);
        startActivity(intent);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }
}