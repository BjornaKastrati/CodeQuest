package com.example.codequest.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConfettiView extends View {

    private final List<Confetti> particles = new ArrayList<>();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean running = false;

    private static final int PARTICLE_COUNT = 80;
    private static final int[] COLORS = {
            0xFFFFD700, 0xFFFF6B6B, 0xFF4ECDC4, 0xFF45B7D1,
            0xFF96CEB4, 0xFFFF8C42, 0xFFB2EBF2, 0xFFCE93D8
    };

    public ConfettiView(Context context) {
        super(context);
    }

    public ConfettiView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void startConfetti() {
        particles.clear();
        running = true;
        post(this::spawnParticles);
        startConfettiAnimation();
    }

    public void stopConfetti() {
        running = false;
        handler.removeCallbacksAndMessages(null);
    }

    private void spawnParticles() {
        int w = getWidth();
        if (w == 0) w = 800;

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            float fromLeft = random.nextBoolean() ? -20 : w + 20;

            particles.add(new Confetti(
                    fromLeft,
                    random.nextFloat() * getHeight(),
                    fromLeft < 0 ? random.nextFloat() * 5f + 2f : -(random.nextFloat() * 5f + 2f),
                    random.nextFloat() * 3f + 1f,
                    random.nextFloat() * 360f,
                    (random.nextFloat() - 0.5f) * 10f,
                    COLORS[random.nextInt(COLORS.length)],
                    8 + random.nextInt(12),
                    4 + random.nextInt(8)
            ));
        }
    }

    private void startConfettiAnimation() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!running) return;

                update();
                invalidate();
                handler.postDelayed(this, 16);
            }
        };

        handler.post(runnable);
    }

    private void update() {
        int w = getWidth();
        int h = getHeight();

        for (Confetti c : particles) {
            c.x += c.vx;
            c.y += c.vy;
            c.vy += 0.08f;
            c.rotation += c.rotSpeed;

            if (c.x < -60 || c.x > w + 60 || c.y > h + 60) {
                boolean left = random.nextBoolean();
                c.x = left ? -20 : w + 20;
                c.y = random.nextFloat() * h;
                c.vx = left ? random.nextFloat() * 5f + 2f : -(random.nextFloat() * 5f + 2f);
                c.vy = random.nextFloat() * 3f + 1f;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Confetti c : particles) {
            paint.setColor(c.color);
            paint.setAlpha(230);

            canvas.save();
            canvas.translate(c.x, c.y);
            canvas.rotate(c.rotation);
            canvas.drawRect(-c.w / 2f, -c.h / 2f, c.w / 2f, c.h / 2f, paint);
            canvas.restore();
        }
    }

    private static class Confetti {
        float x, y, vx, vy, rotation, rotSpeed;
        int color, w, h;

        Confetti(float x, float y, float vx, float vy, float rotation,
                 float rotSpeed, int color, int w, int h) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.rotation = rotation;
            this.rotSpeed = rotSpeed;
            this.color = color;
            this.w = w;
            this.h = h;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopConfetti();
    }
}