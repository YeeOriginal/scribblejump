package com.hbaf.scribblejump;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.hbaf.scribblejump.gameView.GameView;


/** @noinspection CallToPrintStackTrace*/
public class GameThread extends Thread {
    private final SurfaceHolder surfaceHolder;
    private final GameView gameView;
    private boolean running;
    public static final int FPS = 60;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long startTime;     //Время начала кадра
        long msTime;        //Время в мс, надо для расчёта времени ожидания след. кадра
        long sleepTime;     //Время, которое нужно подождать перед отрисовкой следующего кадра
        long targetTime = 1000 / FPS; //Время кадра в миллисекундах ( 16ms для 60 FPS)

        while (running) {
            startTime = System.nanoTime();
            long deltaTime = startTime / 1000000;
            Canvas canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas(); //Начинаем рисовать на поверхности
                //synchronized - блок кода, помеченный этим ключевым словом, будет обрабатываться только одним потоком одновременно
                synchronized (surfaceHolder) {
                    this.gameView.update(deltaTime); //Обновляем игровой цикл
                    this.gameView.draw(canvas); //Отрисовываем объекты на поверхность
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas); //Завершаем редактирование поверхности, показываем обновлённое состояние
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            msTime = (System.nanoTime() - startTime) / 1000000;
            sleepTime = targetTime - msTime;

            try {
                if (sleepTime > 0) {
                    this.sleep(sleepTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
