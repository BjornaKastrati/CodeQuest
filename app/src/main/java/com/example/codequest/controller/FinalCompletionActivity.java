package com.example.codequest.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.net.Uri;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageButton;

import com.example.codequest.R;
import com.example.codequest.util.ProgressManager;

import java.io.File;

public class FinalCompletionActivity extends AppCompatActivity {

    private TextView txtFinalMessage, txtTotalXP;
    private Button btnShareAchievement, btnBackHome;

    private ProgressManager progressManager;
    private int totalXP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_completion);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        progressManager = new ProgressManager(this);
        totalXP = progressManager.getTotalXP();

        txtFinalMessage = findViewById(R.id.txtFinalMessage);
        txtTotalXP = findViewById(R.id.txtTotalXP);
        btnShareAchievement = findViewById(R.id.btnShareAchievement);
        btnBackHome = findViewById(R.id.btnBackHome);

        txtFinalMessage.setText("Computers are officially nervous. You completed the CodeQuest journey!");
        txtTotalXP.setText("Total XP: " + totalXP);

        btnShareAchievement.setOnClickListener(v -> shareAchievement());

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(FinalCompletionActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void shareAchievement() {
        String shareText = "I just completed CodeQuest! 👑\n\n"
                + "I mastered Digital Logic, Programming, and Data Structures through interactive challenges.\n"
                + "Final XP: " + totalXP + "\n\n"
                + "Victory unlocked. Code conquered.";

        File imageFile = new File(
                new File(getCacheDir(), "shared_images"),
                "codequest_victory_selfie.png"
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        if (imageFile.exists()) {
            Uri imageUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    imageFile
            );

            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        }

        startActivity(Intent.createChooser(shareIntent, "Share your CodeQuest victory"));
    }
}