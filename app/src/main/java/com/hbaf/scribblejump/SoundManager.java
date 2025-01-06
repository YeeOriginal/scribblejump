package com.hbaf.scribblejump;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundManager {
    private static SoundManager instance;
    private SoundPool soundPool;
    private int jumpSound, gameOver;
    boolean soundEnabled = true;

    public SoundManager(Context context) {
        // Set up SoundPool with appropriate audio attributes
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5) // Maximum simultaneous sounds
                .setAudioAttributes(audioAttributes)
                .build();

        jumpSound = soundPool.load(context, R.raw.jump, 1);
        gameOver = soundPool.load(context, R.raw.game_over, 1);
    }

    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    public void playJumpSound() {
        if(soundEnabled)
            soundPool.play(jumpSound, 1, 1, 1, 0, 1);
    }

    public void playGameOver() {
        soundPool.play(gameOver, 1, 1, 1, 0, 1);
    }

    public void release() {
        soundPool.release(); // Release resources when done
    }
}