package com.arhiser.wallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.choose_wallpaper).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
            startActivity(intent);
        });
    }
}