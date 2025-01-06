package com.hbaf.scribblejump.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.hbaf.scribblejump.gameView.GameView;
import com.hbaf.scribblejump.R;

public class Platform {
    private float x;
    private float y;
    private final int width = 160, height = 40;
    private final Bitmap bitmapPlatform;

    public Platform(int x, int y, Context context) {
        this.x = x;
        this.y = y;
        this.bitmapPlatform = BitmapFactory.decodeResource(context.getResources(), R.drawable.platform_72);
    }

    // Scroll speed relative to doodle's upward movement
    public void scroll(float offset) {
        y += offset;
    }

    // Ensure the platform is fully within bounds
    public void reset(int x, int y) {
        this.x = Math.max(0, Math.min(x, GameView.getScreenWidth() - width));
        this.y = Math.max(0, Math.min(y, GameView.getScreenHeight() - height));
    }

    public float getY() {
        return y;
    }

    public Rect getRect() {
        return new Rect((int)x, (int)y, (int)x + width, (int)y + height);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        //canvas.drawRect(getRect(), paint);
        canvas.drawBitmap(bitmapPlatform, x, y, paint);
    }
}