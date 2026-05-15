package com.example.codequest.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codequest.R;
import com.example.codequest.model.GameRepository;
import com.example.codequest.model.World;
import com.example.codequest.util.ProgressManager;

public class WorldSelectionActivity extends AppCompatActivity {

    private TextView cardWorld1, cardWorld2, cardWorld3;

    private ProgressManager progressManager;
    private int unlockedWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_selection);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        progressManager = new ProgressManager(this);
        unlockedWorld = progressManager.getUnlockedWorld();

        cardWorld1 = findViewById(R.id.cardWorld1);
        cardWorld2 = findViewById(R.id.cardWorld2);
        cardWorld3 = findViewById(R.id.cardWorld3);

        setupWorldCard(cardWorld1, 1);
        setupWorldCard(cardWorld2, 2);
        setupWorldCard(cardWorld3, 3);
    }

    private void setupWorldCard(TextView card, int worldNumber) {
        World world = GameRepository.getWorld(worldNumber);

        if (worldNumber <= unlockedWorld) {
            card.setText(buildUnlockedWorldText(worldNumber, world));
            card.setAlpha(1f);
            card.setBackgroundResource(R.drawable.card_unlocked);

            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, LevelSelectionActivity.class);
                intent.putExtra("WORLD", worldNumber);
                startActivity(intent);
            });

        } else {
            card.setText(buildLockedWorldText(worldNumber, world));
            card.setAlpha(0.55f);
            card.setBackgroundResource(R.drawable.card_locked);

            card.setOnClickListener(v ->
                    Toast.makeText(this, "Complete the previous world first!", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private String buildUnlockedWorldText(int worldNumber, World world) {
        return getWorldLabel(worldNumber) + "  " + world.getTitle()
                + "\n" + world.getSubtitle()
                + "\n\nUNLOCKED";
    }

    private String buildLockedWorldText(int worldNumber, World world) {
        return getWorldLabel(worldNumber) + "  " + world.getTitle()
                + "\nComplete the previous world first."
                + "\n\nLOCKED";
    }

    private String getWorldLabel(int worldNumber) {
        if (worldNumber == 1) return "01";
        if (worldNumber == 2) return "02";
        return "03";
    }
}