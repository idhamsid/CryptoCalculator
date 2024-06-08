package com.haryo.cryptocalculator.ui;

import static com.haryo.cryptocalculator.isConfig.Settings.REMOTE_ADS;
import static com.haryo.cryptocalculator.isConfig.Settings.URL_DATA;
import static com.haryo.cryptocalculator.isConfig.isAdsConfig.checkConnectivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.haryo.cryptocalculator.R;
import com.haryo.cryptocalculator.isConfig.Settings;
import com.haryo.cryptocalculator.isConfig.isAdsConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (REMOTE_ADS) {
                if (checkConnectivity(SplashActivity.this)) {
                    loadUrlData();
                } else {
                    isAdsConfig.initAds(this);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gotoMain();
                        }
                    },1000);
                }

        }else{
            isAdsConfig.initAds(this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoMain();
                }
            },1000);

        }
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
                        Settings.NATIV= c.getString("admob_native");
                        Settings.BANNER = c.getString("admob_banner");
                        Settings.OPENADS = c.getString("admob_openads");
                        Settings.MAX_BANNER= c.getString("max_banner");
                        Settings.MAX_INTERST = c.getString("max_inter)");
                        Settings.MAX_NATIV = c.getString("max_native");
                        Settings.INTERVAL = c.getInt("interval");
                        gotoMain();

                    }
                } catch (JSONException e) {
                    gotoMain();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                gotoMain();
                Toast.makeText(SplashActivity.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
        requestQueue.add(stringRequest);

    }
    private void gotoMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
