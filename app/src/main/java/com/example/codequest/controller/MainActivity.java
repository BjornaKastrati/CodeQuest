package com.example.codequest.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.codequest.R;
import com.example.codequest.util.ProgressManager;

public class MainActivity extends AppCompatActivity {

    private TextView txtXP;
    private Button btnStartGame, btnSettings;

    private ProgressManager progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        progressManager = new ProgressManager(this);

        txtXP = findViewById(R.id.txtXP);
        btnStartGame = findViewById(R.id.btnStartGame);
        btnSettings = findViewById(R.id.btnSettings);

        txtXP.setText("XP: " + progressManager.getTotalXP());

        btnStartGame.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WorldSelectionActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (progressManager != null && txtXP != null) {
            txtXP.setText("XP: " + progressManager.getTotalXP());
        }
    }
}