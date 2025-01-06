package com.hbaf.scribblejump.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.hbaf.scribblejump.R;

public class LoadingActivity extends AppCompatActivity {
    public static final String EXTRA_TARGET_ACTIVITY = "target_activity";
    public static final String CURRENT_SCORE = "current_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        String targetActivity = getIntent().getStringExtra(EXTRA_TARGET_ACTIVITY);
        int currentScore = getIntent().getIntExtra(CURRENT_SCORE, -1);

        // Start loading the GameActivity
        new LoadGameTask(targetActivity, currentScore).execute();
    }

    private class LoadGameTask extends AsyncTask<Void, Void, Void> {

        private final String targetActivity;
        private final int currentScore;

        public LoadGameTask(String targetActivity, int currentScore) {
            this.targetActivity = targetActivity;
            this.currentScore = currentScore;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent;
            if ("GameActivity".equals(targetActivity)) {
                intent = new Intent(LoadingActivity.this, GameActivity.class);
            } else {
                intent = new Intent(LoadingActivity.this, GameOverActivity.class);
                intent.putExtra("current_score", currentScore);
            }
            startActivity(intent);
            finish();
        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}