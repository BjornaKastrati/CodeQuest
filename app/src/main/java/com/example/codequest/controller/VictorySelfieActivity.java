package com.example.codequest.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codequest.R;
import com.example.codequest.util.ProgressManager;
import com.example.codequest.views.ConfettiView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class VictorySelfieActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;

    private FrameLayout photoContainer;
    private ImageView imgSelfie;
    private TextView txtPhotoPlaceholder, txtVictoryMessage;
    private ConfettiView confettiView;
    private Button btnContinueVictory;

    private int worldNumber;

    private ProgressManager progressManager;
    private MediaPlayer mediaPlayer;

    private boolean cameraAlreadyOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory_selfie);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        worldNumber = getIntent().getIntExtra("WORLD", 1);
        progressManager = new ProgressManager(this);

        connectViews();

        txtVictoryMessage.setText(getRandomVictoryMessage());

        playPhotoAnimation();

        btnContinueVictory.setOnClickListener(v -> continueAfterVictory());

        openCameraOnce();
    }

    private void connectViews() {
        photoContainer = findViewById(R.id.photoContainer);
        imgSelfie = findViewById(R.id.imgSelfie);
        txtPhotoPlaceholder = findViewById(R.id.txtPhotoPlaceholder);
        txtVictoryMessage = findViewById(R.id.txtVictoryMessage);
        confettiView = findViewById(R.id.confettiView);
        btnContinueVictory = findViewById(R.id.btnContinueVictory);
    }

    private void openCameraOnce() {
        if (cameraAlreadyOpened) return;

        cameraAlreadyOpened = true;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            txtPhotoPlaceholder.setText("Camera app not available.\nVictory still achieved!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {

            Bundle extras = data.getExtras();

            if (extras != null) {

                Bitmap imageBitmap = (Bitmap) extras.get("data");

                if (imageBitmap != null) {

                    imgSelfie.setImageBitmap(imageBitmap);

                    txtPhotoPlaceholder.setVisibility(View.GONE);

                    playPhotoAnimation();

                    startVictoryCelebration();

                    saveVictoryPhotoForSharing();
                }
            }

        } else {

            txtPhotoPlaceholder.setText("Selfie skipped.\nVictory still counts!");

            startVictoryCelebration();
        }
    }

    private void playPhotoAnimation() {

        if (!progressManager.isAnimationEnabled()) return;

        photoContainer.setAlpha(0f);
        photoContainer.setScaleX(0.92f);
        photoContainer.setScaleY(0.92f);

        photoContainer.animate()
                .alpha(1f)
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(900)
                .start();
    }

    private void startVictoryCelebration() {

        if (progressManager.isAnimationEnabled()) {
            confettiView.startConfetti();
        }

        playCelebrationSound();
    }

    private void playCelebrationSound() {

        if (!progressManager.isSoundEnabled()) return;

        mediaPlayer = MediaPlayer.create(this, R.raw.celebration_chime);

        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void saveVictoryPhotoForSharing() {

        photoContainer.postDelayed(() -> {

            Bitmap bitmap = Bitmap.createBitmap(
                    photoContainer.getWidth(),
                    photoContainer.getHeight(),
                    Bitmap.Config.ARGB_8888
            );

            Canvas canvas = new Canvas(bitmap);

            photoContainer.draw(canvas);

            File folder = new File(getCacheDir(), "shared_images");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, "codequest_victory_selfie.png");

            try (FileOutputStream outputStream = new FileOutputStream(file)) {

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }, 1200);
    }

    private String getRandomVictoryMessage() {

        String[] messages;

        if (worldNumber == 1) {

            messages = new String[]{
                    "Oh My Gates! You’re a logic legend! ⚡",
                    "AND you did it… OR maybe you were always this good!",
                    "XOR-ordinary performance. Truly unmatched.",
                    "You flipped every bit perfectly. Respect.",
                    "Logic doesn’t stand a chance against you.",
                    "Circuits fear your intelligence."
            };

        } else if (worldNumber == 2) {

            messages = new String[]{
                    "This is what a Programming God looks like 💻",
                    "No bugs detected. Only brilliance.",
                    "You don’t debug… bugs run from YOU.",
                    "Clean code. Clean victory.",
                    "Even the compiler is impressed.",
                    "Syntax? Flawless. Logic? Elite."
            };

        } else {

            messages = new String[]{
                    "Data structures? More like data conquered.",
                    "Even trees bow to your knowledge 🌳",
                    "Perfect structure. Perfect victory.",
                    "Algorithms whisper your name.",
                    "Neurons stacked. Achievements queued.",
                    "Dijkstra is recalculating… because you found the shortest path to victory."
            };
        }

        Random random = new Random();

        return messages[random.nextInt(messages.length)];
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

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (confettiView != null) {
            confettiView.stopConfetti();
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}