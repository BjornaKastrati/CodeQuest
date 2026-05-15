package com.example.codequest.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.codequest.R;
import com.example.codequest.util.ProgressManager;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchSound, switchAnimations;
    private Button btnResetProgress, btnBackHome;

    private ProgressManager progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        progressManager = new ProgressManager(this);

        switchSound = findViewById(R.id.switchSound);
        switchAnimations = findViewById(R.id.switchAnimations);
        btnResetProgress = findViewById(R.id.btnResetProgress);
        btnBackHome = findViewById(R.id.btnBackHome);

        switchSound.setChecked(progressManager.isSoundEnabled());
        switchAnimations.setChecked(progressManager.isAnimationEnabled());

        switchSound.setOnCheckedChangeListener((buttonView, isChecked) ->
                progressManager.setSoundEnabled(isChecked)
        );

        switchAnimations.setOnCheckedChangeListener((buttonView, isChecked) ->
                progressManager.setAnimationEnabled(isChecked)
        );

        btnResetProgress.setOnClickListener(v -> confirmReset());

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void confirmReset() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Progress?")
                .setMessage("This will lock worlds and levels again and reset XP/stars.")
                .setPositiveButton("Reset", (dialog, which) -> {
                    progressManager.resetProgress();
                    Toast.makeText(this, "Progress reset!", Toast.LENGTH_SHORT).show();

                    switchSound.setChecked(progressManager.isSoundEnabled());
                    switchAnimations.setChecked(progressManager.isAnimationEnabled());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}