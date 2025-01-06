package com.hbaf.scribblejump.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.hbaf.scribblejump.R;
import com.hbaf.scribblejump.SoundManager;

public class GameOverActivity extends AppCompatActivity {

    private static final String HIGH_SCORE_KEY = "high_score";
    private static final String CURRENT_SCORE = "current_score";
    private static final String PREFS_NAME = "prefs";

    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        SoundManager soundManager = SoundManager.getInstance(this);

        int currentScore = getIntent().getIntExtra(CURRENT_SCORE, -1);

        Button restartGameButton = findViewById(R.id.restart_game_button);
        Button mainMenuButton = findViewById(R.id.main_menu_button);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int highScore = sharedPreferences.getInt(HIGH_SCORE_KEY, 0);

        TextView currentScoreText = findViewById(R.id.current_score);
        currentScoreText.setText("Score: " + currentScore);
        TextView currentHighcoreText = findViewById(R.id.current_high_score);
        currentHighcoreText.setText("Current high score: " + highScore);

        restartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, LoadingActivity.class);
                intent.putExtra(LoadingActivity.EXTRA_TARGET_ACTIVITY, "GameActivity");
                startActivity(intent);
                finish();
            }
        });
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(i -> {
            if (i == 0)
                decorView.setSystemUiVisibility(hideSystemBars());
        });
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