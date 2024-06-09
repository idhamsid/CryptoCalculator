package com.haryo.cryptocalculator.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haryo.cryptocalculator.R;
import com.haryo.cryptocalculator.adapter.CoinHistoryAdapter;
import com.haryo.cryptocalculator.isConfig.SharedPreference;
import com.haryo.cryptocalculator.isConfig.isAdsConfig;
import com.haryo.cryptocalculator.modul.DataCrypto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CoinHistory extends AppCompatActivity {
    private RecyclerView recCoinHistory;
    ArrayList<DataCrypto> coinLists;
    SharedPreference sharedPref;
    CoinHistoryAdapter adapter;
    GridLayoutManager mLayoutManager;
    RelativeLayout adsBanner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        sharedPref = new SharedPreference();
        coinLists = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        adsBanner = findViewById(R.id.adsBanner);
        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("History");
            }
        } catch (Exception e) {
            Log.i("adslog", "exception : " + e.getMessage());
        }
        recCoinHistory = findViewById(R.id.recCoinHistory);
        mLayoutManager = new GridLayoutManager(this, 1);
        recCoinHistory.setLayoutManager(mLayoutManager);
        isAdsConfig.callBanner(this,adsBanner);
        isAdsConfig.loadInters(this,false);
        coinLists = sharedPref.getCoins(this);
        Collections.reverse(coinLists);
        adapter = new CoinHistoryAdapter(coinLists,CoinHistory.this);
        recCoinHistory.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
