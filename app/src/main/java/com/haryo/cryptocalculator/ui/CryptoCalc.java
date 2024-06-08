package com.haryo.cryptocalculator.ui;


import static com.haryo.cryptocalculator.isConfig.Settings.OPENADS;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.lazygeniouz.aoa.AppOpenAdManager;
import com.lazygeniouz.aoa.configs.Configs;
import com.lazygeniouz.aoa.idelay.InitialDelay;


public class CryptoCalc extends Application {

    private static CryptoCalc mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences openadsSharedPref = getSharedPreferences("openads", Context.MODE_PRIVATE);
        OPENADS = openadsSharedPref.getString("admobopenads",OPENADS);
        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });

        mInstance = this;
        AppOpenAdManager adManager = AppOpenAdManager.get(
                this,
                new Configs(
                        InitialDelay.NONE,
                        OPENADS,
                        new AdRequest.Builder().build(),
                        () -> true
                )
        );
        adManager.loadAppOpenAd();

    }

    public static synchronized CryptoCalc getInstance() {
        return mInstance;
    }

}