package com.hbaf.scribblejump.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.hbaf.scribblejump.R;
import com.hbaf.scribblejump.SoundManager;

public class Doodle {
    //Константы
    private final float GRAVITY = 0.85F;   //Default: 0.85F
    private final int JUMP_VELOCITY = -30; //Default: -30
    private final int WIDTH = 128, HEIGHT = 32; //Размер хитбокса
    private final long FRAME_DELAY = 1000; //По идее, это должна быть секундная задержка между сменой спрайтов, но она слишком почему-то быстрая
    private final int FRAME_AMOUNT = 6;

    private int x;
    private int y;
    private float dy; // Vertical velocity
    private boolean falling; // Falling state

    private Bitmap[] bitmapScribble = new Bitmap[FRAME_AMOUNT];
    private int currentFrame; // Index of the current frame
    private long frameTimer; // Timer to control frame switching

    private SoundManager soundManager;

    public void init(int x, int y, Context context, SoundManager soundManager) {
        this.x = x;
        this.y = y;
        this.dy = 0;
        this.falling = true;
        this.bitmapScribble[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.body1_64);
        this.bitmapScribble[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.body2_64);
        this.bitmapScribble[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.body3_64);
        this.bitmapScribble[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.body4_64);
        this.bitmapScribble[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.body5_64);
        this.bitmapScribble[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.body6_64);
        this.soundManager = soundManager;
    }

    public void update(long deltaTime) {
        dy += GRAVITY; //Гравитация
        y += (int) dy; //Обновление вертикального положения

        frameTimer += deltaTime; // Increment timer with the time passed
        if (frameTimer >= FRAME_DELAY) {
            currentFrame = (currentFrame + 1) % bitmapScribble.length; // Cycle through frames
            frameTimer = 0; // Reset timer
        }

        //Определяем падает ли игрок
        falling = dy > 0;
    }

    public void jump() {
        dy = JUMP_VELOCITY; // Jump velocity
        soundManager.playJumpSound();
    }

    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return y;
    }

    public float getDy() {
        return dy;
    }

    public int getWidth() {
        return WIDTH;
    }

    public boolean isFalling() {
        return falling;
    }

    public Rect getRect() {
        return new Rect(x, y, x + WIDTH, y + HEIGHT);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        canvas.drawBitmap(bitmapScribble[currentFrame], x, y- HEIGHT *3, paint);
        //canvas.drawRect((float)x , (float)y, (float)x+(float)width, (float)y +(float)height, paint);
    }
}