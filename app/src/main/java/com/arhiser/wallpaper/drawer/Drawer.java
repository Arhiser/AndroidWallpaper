package com.arhiser.wallpaper.drawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.arhiser.wallpaper.drawer.noise.FractalNoise;
import com.arhiser.wallpaper.drawer.noise.Random;

public class Drawer {

    Bitmap bitmap;

    Paint paint = new Paint();

    int[] pixels;

    int shift;

    FractalNoise noise;

    public void draw(Canvas canvas) {
        if (!initFrame(canvas)) {
            bitmap.getPixels(pixels, 0, canvas.getWidth(), 1, 0,
                    canvas.getWidth() - 1, canvas.getHeight());

            for (int i = 0; i < canvas.getHeight(); i++) {
                int pixelIndex = i * canvas.getWidth() + canvas.getWidth() - 1;
                int value = 0xff & (int)(noise.getValue(canvas.getWidth() - 1 + shift, i) * 255);
                value = (int) Math.min(value + 64 * ((float)i / canvas.getHeight()), 255);
                pixels[pixelIndex] = 0xff000000 | value << 16 | value << 8 | 255;
            }

            bitmap.setPixels(pixels, 0, canvas.getWidth(), 0, 0,
                    canvas.getWidth(), canvas.getHeight());
            canvas.drawBitmap(bitmap, 0, 0, paint);
            shift += 1;
        }
    }

    private boolean initFrame(Canvas canvas) {
        if (bitmap == null
                || bitmap.getWidth() != canvas.getWidth()
                || bitmap.getHeight() != canvas.getHeight()
                || bitmap.isRecycled()) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }

            bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            pixels = new int[canvas.getWidth() * canvas.getHeight()];
            noise = new FractalNoise(512, new Random(canvas.getWidth(), 1000), 9);

            int pixelIndex = 0;
            for (int i = 0; i < canvas.getHeight(); i++) {
                for (int j = 0; j < canvas.getWidth(); j++) {
                    int value = 0xff & (int)(noise.getValue(j + shift, i) * 255);
                    value = (int) Math.min(value + 64 * ((float)i / canvas.getHeight()), 255);
                    pixels[pixelIndex++] = 0xff000000 | value << 16 | value << 8 | 255;
                }
            }
            bitmap.setPixels(pixels, 0, canvas.getWidth(), 0, 0,
                    canvas.getWidth(), canvas.getHeight());
            canvas.drawBitmap(bitmap, 0, 0, paint);

            return true;
        }
        return false;
    }
}
