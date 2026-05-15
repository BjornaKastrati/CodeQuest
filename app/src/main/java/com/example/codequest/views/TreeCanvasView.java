package com.example.codequest.views;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TreeCanvasView extends View {

    public interface OnNodeTapListener {
        void onNodeTapped(int nodeValue, int nodeIndex);
    }

    private OnNodeTapListener listener;

    private static final int[] NODE_VALUES = {8, 4, 12, 2, 6, 14};

    private final float[] nx = {0.5f, 0.25f, 0.75f, 0.125f, 0.375f, 0.875f};
    private final float[] ny = {0.14f, 0.38f, 0.38f, 0.66f, 0.66f, 0.66f};

    private final int[][] edges = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}
    };

    private final List<Integer> tappedIndices = new ArrayList<>();
    private boolean allCorrect = false;

    private final Paint edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint nodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint orderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public TreeCanvasView(Context context) {
        super(context);
        init();
    }

    public TreeCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundColor(0xFF0D2137);

        edgePaint.setColor(0xFF90A4AE);
        edgePaint.setStrokeWidth(5f);
        edgePaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        orderPaint.setColor(0xFFFFD600);
        orderPaint.setTextAlign(Paint.Align.CENTER);
        orderPaint.setTypeface(Typeface.DEFAULT_BOLD);

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(4f);
    }

    public void setOnNodeTapListener(OnNodeTapListener listener) {
        this.listener = listener;
    }

    public void addTappedIndex(int index) {
        if (!tappedIndices.contains(index)) {
            tappedIndices.add(index);
            invalidate();
        }
    }

    public void clearTapped() {
        tappedIndices.clear();
        allCorrect = false;
        invalidate();
    }

    public void setAllCorrect() {
        allCorrect = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();
        float nodeR = Math.min(w, h) * 0.085f;

        for (int[] edge : edges) {
            float x1 = nx[edge[0]] * w;
            float y1 = ny[edge[0]] * h;
            float x2 = nx[edge[1]] * w;
            float y2 = ny[edge[1]] * h;
            canvas.drawLine(x1, y1, x2, y2, edgePaint);
        }

        for (int i = 0; i < NODE_VALUES.length; i++) {
            float cx = nx[i] * w;
            float cy = ny[i] * h;

            boolean tapped = tappedIndices.contains(i);

            if (allCorrect) {
                nodePaint.setColor(0xFF1B5E20);
                borderPaint.setColor(0xFF69F0AE);
            } else if (tapped) {
                nodePaint.setColor(0xFF00838F);
                borderPaint.setColor(0xFF00E5FF);
            } else {
                nodePaint.setColor(0xFF1565C0);
                borderPaint.setColor(0xFF42A5F5);
            }

            canvas.drawCircle(cx, cy, nodeR, nodePaint);
            canvas.drawCircle(cx, cy, nodeR, borderPaint);

            textPaint.setTextSize(nodeR * 0.85f);
            canvas.drawText(String.valueOf(NODE_VALUES[i]), cx, cy + nodeR * 0.3f, textPaint);

            if (tapped) {
                int order = tappedIndices.indexOf(i) + 1;
                orderPaint.setTextSize(nodeR * 0.55f);
                canvas.drawText(String.valueOf(order), cx + nodeR * 0.8f, cy - nodeR * 0.75f, orderPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) return true;

        float w = getWidth();
        float h = getHeight();
        float nodeR = Math.min(w, h) * 0.085f;

        for (int i = 0; i < NODE_VALUES.length; i++) {
            float cx = nx[i] * w;
            float cy = ny[i] * h;

            float dx = event.getX() - cx;
            float dy = event.getY() - cy;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= nodeR * 1.4f && !tappedIndices.contains(i)) {
                if (listener != null) {
                    listener.onNodeTapped(NODE_VALUES[i], i);
                }
                return true;
            }
        }

        return true;
    }
}