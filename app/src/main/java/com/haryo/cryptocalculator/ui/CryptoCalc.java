package com.haryo.cryptocalculator.ui;


import static com.haryo.cryptocalculator.isConfig.Settings.OPENADS;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.lazygeniouz.aoa.AppOpenAdManager;
import com.lazygeniouz.aoa.configs.Configs;
import com.lazygeniouz.aoa.idelay.InitialDelay;


public class CryptoCalc extends Application {
    private static CryptoCalc mInstance;
    public static AppOpenAdManager appOpenAdManager;
    public static String openads;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences myScore = getSharedPreferences("openads", Context.MODE_PRIVATE);
        OPENADS = myScore.getString("admobopenads", OPENADS);
        Configs configs = new Configs(InitialDelay.NONE,
                OPENADS,
                new AdRequest.Builder().build(),
                () -> true);
        appOpenAdManager = AppOpenAdManager.get(this, configs);
        mInstance = this;
    }

    public static synchronized CryptoCalc getInstance() {
        return mInstance;
    }

}