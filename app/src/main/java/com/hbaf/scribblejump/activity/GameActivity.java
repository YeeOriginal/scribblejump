package com.hbaf.scribblejump.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.hbaf.scribblejump.gameView.GameView;
import com.hbaf.scribblejump.gameView.GameViewListener;
import com.hbaf.scribblejump.SoundManager;

public class GameActivity extends AppCompatActivity implements GameViewListener {

    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this, this));

        SoundManager soundManager = SoundManager.getInstance(this);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(i -> {
            if (i == 0)
                decorView.setSystemUiVisibility(hideSystemBars());
        });
    }

    @Override
    public void onGameOver(int currentScore) {
        Intent intent = new Intent(this, LoadingActivity.class);
        intent.putExtra("current_score", currentScore);
        startActivity(intent);
        finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {}

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

}

