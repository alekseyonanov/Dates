package com.nollpointer.dates.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class VisualizerView extends View {
    public static final String TAG = "VisualizerView";

    public static final float AMPLITUDE_MAX_VALUE = 15f;
    public static final float AMPLITUDE_MIN_VALUE = 0f;

    private Paint linePaint;

    private float amplitude = AMPLITUDE_MIN_VALUE;

    private int width;
    private int height;

    private boolean isHidden = false;


    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint();
        linePaint.setColor(0xff448aff);
        linePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    //выставление амплитуды
    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
        invalidate();
    }

    public void hide() {
        this.amplitude = AMPLITUDE_MIN_VALUE;
        isHidden = true;
        invalidate();
    }

    public void show() {
        isHidden = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        int halfWidth = width / 2;
        float delta = 0f;

        if (!isHidden)
            delta = halfWidth * (amplitude / AMPLITUDE_MAX_VALUE);


        for (int i = 0; i < height; i++)
            canvas.drawLine(halfWidth - delta, i, halfWidth + delta, i, linePaint);

    }
}