package com.haryo.cryptocalculator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.haryo.cryptocalculator.R;
import com.haryo.cryptocalculator.isConfig.isAdsConfig;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isAdsConfig.initAds(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoMain();
            }
        },1000);
    }

    private void gotoMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
