package com.hbaf.scribblejump.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

//Floor for testing if jumps and camera work correctly
public class TestFloor {
    private final int x;
    private final int y;
    private final int width = 3000, height = 20;

    public TestFloor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Rect getRect() {
        return new Rect(x, y, x + width, y + height);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(getRect(), paint);
    }
}