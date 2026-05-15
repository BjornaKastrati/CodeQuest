package com.example.codequest.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codequest.R;
import com.example.codequest.model.GameRepository;
import com.example.codequest.model.World;

public class VictorySelfieIntroActivity extends AppCompatActivity {

    private TextView txtVictoryTitle, txtVictoryMessage;
    private Button btnTakeSelfie, btnSkipSelfie;

    private int worldNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory_selfie_intro);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        worldNumber = getIntent().getIntExtra("WORLD", 1);

        txtVictoryTitle = findViewById(R.id.txtVictoryTitle);
        txtVictoryMessage = findViewById(R.id.txtVictoryMessage);
        btnTakeSelfie = findViewById(R.id.btnTakeSelfie);
        btnSkipSelfie = findViewById(R.id.btnSkipSelfie);

        World world = GameRepository.getWorld(worldNumber);

        txtVictoryTitle.setText(world.getTitle() + " Complete!");
        txtVictoryMessage.setText(getWorldCompletionMessage(worldNumber));

        btnTakeSelfie.setOnClickListener(v -> {
            Intent intent = new Intent(VictorySelfieIntroActivity.this, VictorySelfieActivity.class);
            intent.putExtra("WORLD", worldNumber);
            startActivity(intent);
            finish();
        });

        btnSkipSelfie.setOnClickListener(v -> continueAfterVictory());
    }

    private String getWorldCompletionMessage(int world) {
        if (world == 1) {
            return "You conquered gates, binary lights, and logic circuits.";
        } else if (world == 2) {
            return "You mastered Java outputs, syntax, ordering, and clean code.";
        } else {
            return "You defeated arrays, stacks, queues, and trees.";
        }
    }

    private void continueAfterVictory() {
        if (worldNumber == 3) {
            Intent intent = new Intent(this, FinalCompletionActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, WorldSelectionActivity.class);
            startActivity(intent);
        }

        finish();
    }
}