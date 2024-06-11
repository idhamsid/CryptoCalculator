package com.haryo.cryptocalculator.ui;

import static com.haryo.cryptocalculator.BuildConfig.DEBUG;
import static com.haryo.cryptocalculator.isConfig.Settings.ONESIGNAL_APP_ID;
import static com.haryo.cryptocalculator.isConfig.Settings.REMOTE_ADS;
import static com.haryo.cryptocalculator.isConfig.Settings.URL_DATA;
import static com.haryo.cryptocalculator.isConfig.isAdsConfig.checkConnectivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
import com.haryo.cryptocalculator.R;
import com.haryo.cryptocalculator.isConfig.Settings;
import com.haryo.cryptocalculator.isConfig.isAdsConfig;
import com.lazygeniouz.aoa.listener.AppOpenAdListener;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    Handler mainHandler = new Handler();
    static Boolean splash, openeShow, openMain = false;
    final Runnable timerForceMain = new Runnable() {
        public void run() {
            gotoMain();
        }
    };

    private void callopenAds() {
        CryptoCalc.appOpenAdManager.setAppOpenAdListener(new AppOpenAdListener() {
            @Override
            public void onAdDismissed() {
                super.onAdDismissed();
                if (!splash) {
                    if (!openMain)
                        gotoMain();
                } else gotoMain();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if (splash)
                    gotoMain();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
//                if (splash)
                mainHandler.removeCallbacks(timerForceMain);
            }

            @Override
            public void onAdShowFailed(@Nullable AdError error) {
                super.onAdShowFailed(error);
                if (splash)
                    gotoMain();
            }

            @Override
            public void onAdShown() {
                super.onAdShown();
                openeShow = true;
                if (splash)
                    mainHandler.removeCallbacks(timerForceMain);
            }

            @Override
            public void onAdWillShow() {
                super.onAdWillShow();
                if (splash)
                    mainHandler.removeCallbacks(timerForceMain);
            }
        });
        CryptoCalc.appOpenAdManager.loadAppOpenAd();
    }

    @Override
    public void onStart() {
        super.onStart();
        splash = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        splash = false;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (DEBUG) {
            OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        }
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);
        OneSignal.getNotifications().requestPermission(false, Continue.none());
        if (REMOTE_ADS) {
            if (checkConnectivity(SplashActivity.this)) {
                loadUrlData();
            } else {
                isAdsConfig.initAds(this);
                callopenAds();
            }

        } else {
            isAdsConfig.initAds(this);
            callopenAds();
        }
        mainHandler.postDelayed(timerForceMain, 8000);
    }

    private void loadUrlData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray contacts = jsonObj.getJSONArray("Ads");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        Settings.INTER = c.getString("admod_inter");
                        Settings.NATIV = c.getString("admob_native");
                        Settings.BANNER = c.getString("admob_banner");
                        Settings.OPENADS = c.getString("admob_openads");
                        Settings.MAX_BANNER = c.getString("max_banner");
                        Settings.MAX_INTERST = c.getString("max_inter)");
                        Settings.MAX_NATIV = c.getString("max_native");
                        Settings.INTERVAL = c.getInt("interval");
                        callopenAds();

                    }
                } catch (JSONException e) {
                    callopenAds();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callopenAds();
                Toast.makeText(SplashActivity.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
        requestQueue.add(stringRequest);

    }

    private void gotoMain() {
        Log.w("adslog", "gogtomain:  ");
        openMain = true;
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
