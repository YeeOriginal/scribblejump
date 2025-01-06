package com.hbaf.scribblejump.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.hbaf.scribblejump.R;
import com.hbaf.scribblejump.SoundManager;

public class MainMenuActivity extends AppCompatActivity {

    private View decorView;
    SoundManager soundManager;
    private CheckBox soundCheckbox;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "prefs";
    private static final String SOUND_KEY = "sound_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);

        soundManager = SoundManager.getInstance(this);
        soundCheckbox = findViewById(R.id.sound_checkbox);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Загружаем последнии настройки
        boolean isSoundEnabled = sharedPreferences.getBoolean(SOUND_KEY, true);
        soundCheckbox.setChecked(isSoundEnabled);
        SoundManager.getInstance(this).setSoundEnabled(isSoundEnabled);

        // Слушатель чекбокса
        soundCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Сохраняем настройки звука
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SOUND_KEY, isChecked);
            editor.apply();

            // Применяем настройки звука
            SoundManager.getInstance(this).setSoundEnabled(isChecked);
        });

        Button startGameButton = findViewById(R.id.play_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, LoadingActivity.class);
                intent.putExtra(LoadingActivity.EXTRA_TARGET_ACTIVITY, "GameActivity");
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
    public void onBackPressed() { finish(); }

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