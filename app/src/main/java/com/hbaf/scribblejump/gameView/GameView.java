package com.hbaf.scribblejump.gameView;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hbaf.scribblejump.GameThread;
import com.hbaf.scribblejump.R;
import com.hbaf.scribblejump.SoundManager;
import com.hbaf.scribblejump.entity.Doodle;
import com.hbaf.scribblejump.entity.Platform;
import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    private final GameThread gameThread;

    private final Doodle doodle;
    private final List<Platform> platforms;

    private static int screenWidth, screenHeight;
    private final GameViewListener listener;

    // Сенсор
    private final SensorManager sensorManager;
    private Sensor accelerometer;
    private float tiltX = 0; // Значение наклона по Х оси

    private Paint scorePaint;
    private int currentScore = 0;
    private int highScore = 0;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "prefs";
    private static final String HIGH_SCORE_KEY = "high_score";

    public GameView(Context context, GameViewListener listener) {
        super(context);
        this.listener = listener;
        getHolder().addCallback(this); //Вызываем через getHolder() наш Canvas для отрисовки
        gameThread = new GameThread(getHolder(), this);

        doodle = new Doodle();
        platforms = new ArrayList<>();

        sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        highScore = sharedPreferences.getInt(HIGH_SCORE_KEY, 0);

        scorePaint = new Paint();
        scorePaint.setColor(Color.BLACK); // Set text color to white
        scorePaint.setTextSize(50); // Set text size to 50 pixels
        scorePaint.setAntiAlias(true);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    public static int getScreenWidth() {
        return screenWidth;
    }
    public static int getScreenHeight() {
        return screenHeight;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenWidth = getWidth();
        screenHeight = getHeight();

        doodle.init(screenWidth / 2, screenHeight / 2, getContext(), SoundManager.getInstance(getContext()));

        // Creating initial platforms
        for (int i = 0; i < 10; i++) {
            int x = (int) (Math.random() * (screenWidth - 200)); // Ensure full width fits
            int y = screenHeight - i * 200; // Space platforms vertically
            platforms.add(new Platform(x, y, getContext()));
        }
        platforms.add(new Platform(screenWidth/2, screenHeight/2, getContext()));

        // Начинаем получать обновления сенсора
        if (accelerometer != null) { sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME); }

        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);

        // Перестаем слушать обновление сенсора
        sensorManager.unregisterListener(this);

        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(long deltaTime) {

        doodle.update(deltaTime);

        if (doodle.getY() > screenHeight) {
            // Launch the Game Over screen
            gameThread.setRunning(false);
            gameOver();
        }
        
        //--------------------ДВИЖЕНИЕ ПО X--------------------

        final int factor = 7; //Множитель для скорости движения
        int velocity = (int) tiltX * factor; //Скорость движения
        doodle.setX(doodle.getX() + velocity);

        // Переход из одного края экрана в другой
        if (doodle.getX() + doodle.getWidth() < 0) {
            doodle.setX(screenWidth);
        } else if (doodle.getX() > screenWidth) {
            doodle.setX(-doodle.getWidth());
        }

        //--------------------ДВИЖЕНИЕ ПО Y--------------------

        // Check if we need to move the camera (doodle goes above the halfway point)
        int scrollOffset = 0;

        if (doodle.getY() < screenHeight / 2) {
            // Calculate how far the camera should move
            scrollOffset = (screenHeight / 2) - doodle.getY();
            updateScore(scrollOffset);

            // Lock doodle's Y position to the halfway point
            doodle.setY(screenHeight / 2);
        }

        // Scroll platforms and other objects by the camera offset
        for (Platform platform : platforms) {
            platform.scroll(scrollOffset);

            // Reset platforms if they move off the bottom of the screen
            if (platform.getY() > screenHeight) {
                platform.reset((int) (Math.random() * screenWidth), 0);
            }
        }

        //--------------------КОЛЛИЗИЯ--------------------

        for (Platform platform : platforms) {
            if (doodle.isFalling() && doodle.getDy() > 0 && doodle.getRect().intersect(platform.getRect())) {
                doodle.jump(); // Прыжок при коллизии с платформой
            }
        }
        //-----------------------------------------------------
    }

    private void updateScore(int scrollOffset) {
        currentScore += scrollOffset; // Calculate score
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {

            canvas.drawColor(Color.WHITE);

            Bitmap backgroundGrid = BitmapFactory.decodeResource(getResources(), R.drawable.background_grid_128);
            Bitmap backgroundMarginGrid = BitmapFactory.decodeResource(getResources(), R.drawable.backround_margin_grid_128);
            int gridHeight = backgroundGrid.getHeight();
            int gridWidth = backgroundGrid.getWidth();

            for (int i = 0; i <= screenHeight/gridHeight; i++) {
                canvas.drawBitmap(backgroundMarginGrid, 0, i*gridHeight , null);
            }

            for (int i = 0; i <= screenHeight/gridHeight; i++) {
                for (int j = 1; j <= screenWidth/gridWidth; j++) {
                    canvas.drawBitmap(backgroundGrid, j * gridWidth, i * gridHeight, null);
                }
            }

            for (Platform platform : platforms) { platform.draw(canvas);}
            doodle.draw(canvas);

            canvas.drawText("Score: " + currentScore, screenWidth - 350, 100, scorePaint);
        }
    }

    private float filteredTiltX = 0; // Сглаженное значение наклона

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float rawTiltX = event.values[0]; // Наклон по X оси

            // Применяем low-pass фильтр
            float alpha = 0.1f; // Множитель сглаживания
            filteredTiltX = alpha * rawTiltX + (1 - alpha) * filteredTiltX;

            // Optional: Invert X-axis if needed (depending on the device orientation)
            tiltX = -filteredTiltX;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void gameOver() {
        if (listener != null) {
            if(currentScore > highScore) {
                highScore = currentScore;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(HIGH_SCORE_KEY, highScore);
                editor.apply();
            }
            listener.onGameOver(currentScore); // Notify the listener
        }
    }
}
