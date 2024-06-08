package com.haryo.cryptocalculator.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.haryo.cryptocalculator.R;
import com.haryo.cryptocalculator.isConfig.isAdsConfig;

public class SpotProfitActivity extends AppCompatActivity {
    TextInputEditText amount, buyPriceText, sellPriceText, feeBrokerText;
    TextInputEditText profitText, profitPersenText, totalMoneyText;
    MaterialButton submitButton, clearButton;
    float amountFloat, buyFloat, sellFloat, feeFloat, profit, profitPersen, totalMoney;
    RelativeLayout adsBanner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_profit);

        amount = findViewById(R.id.amountText);
        buyPriceText = findViewById(R.id.buyPriceText);
        sellPriceText = findViewById(R.id.sellPriceText);
        feeBrokerText = findViewById(R.id.feeBrokerText);

        submitButton = findViewById(R.id.submitButton);
        clearButton = findViewById(R.id.refreshBtn);


        profitText = findViewById(R.id.profitText);
        profitPersenText = findViewById(R.id.profitPersenText);
        totalMoneyText = findViewById(R.id.totalMoneyText);

        Toolbar toolbar = findViewById(R.id.toolbar);
        adsBanner = findViewById(R.id.adsBanner);
        isAdsConfig.callBanner(this,adsBanner);
        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Spot Profit");
            }
        } catch (Exception e) {
            Log.i("adslog", "exception : " + e.getMessage());
        }
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.getText().clear();
                buyPriceText.getText().clear();
                sellPriceText.getText().clear();
                feeBrokerText.getText().clear();
                profitText.setText("");
                profitPersenText.setText("");
                totalMoneyText.setText("");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().equals("") || buyPriceText.getText().toString().equals("") ||
                        sellPriceText.getText().toString().equals("") || feeBrokerText.getText().toString().equals("")) {
                    Snackbar.make(findViewById(R.id.main_content), "Please fill the form", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.purple_700)).show();
                } else {
                    amountFloat = Float.parseFloat(amount.getText().toString());
                    buyFloat = Float.parseFloat(buyPriceText.getText().toString());
                    sellFloat = Float.parseFloat(sellPriceText.getText().toString());
                    feeFloat = Float.parseFloat(feeBrokerText.getText().toString());

                    profit = (amountFloat * sellFloat) - (amountFloat * buyFloat) - (feeFloat * sellFloat);
                    profitPersen = ((sellFloat - buyFloat) / buyFloat) * 100;
                    totalMoney = amountFloat + profit;

                    if (profit < 0) {
                        profitText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
                    }
                    if (profitPersen < 0) {
                        profitPersenText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
                    }
                    if (totalMoney < 0) {
                        totalMoneyText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
                    }
                    profitText.setText(String.valueOf(profit));
                    profitPersenText.setText(String.valueOf(profitPersen));
                    totalMoneyText.setText(String.valueOf(totalMoney));
                }

            }
        });
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
