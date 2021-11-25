package com.arhiser.wallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.arhiser.wallpaper.drawer.Drawer;

public class WallpaperService extends android.service.wallpaper.WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    class WallpaperEngine extends Engine {

        SurfaceHolder surfaceHolder;

        Handler handler;

        Drawer drawer = new Drawer();

        Runnable redrawRunnable = new Runnable() {
            @Override
            public void run() {
                draw();
                handler.postDelayed(this, 10);
            }
        };

        private void draw() {
            if (surfaceHolder != null) {
                try {
                    Canvas canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        drawer.draw(canvas);
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } catch (Exception exception) {
                    Log.e("wallpaper", exception.getMessage(), exception);
                }
            }
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.surfaceHolder = surfaceHolder;

            handler = new Handler();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            handler.removeCallbacks(redrawRunnable);
            if (visible) {
                handler.post(redrawRunnable);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            surfaceHolder = holder;
        }

        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);
            surfaceHolder = holder;
            draw();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            surfaceHolder = holder;
            handler.post(redrawRunnable);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            handler.removeCallbacks(redrawRunnable);
        }
    }
}
