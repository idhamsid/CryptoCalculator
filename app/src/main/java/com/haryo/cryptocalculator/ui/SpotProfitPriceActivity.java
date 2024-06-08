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

public class SpotProfitPriceActivity extends AppCompatActivity {

    TextInputEditText amountText,buyPriceText,profitText;
    TextInputEditText sellPriceText,profitText2;
    MaterialButton submitButton,refreshBtn;
    long amountFloat,buyPriceFloat,profitFloat,sellPricefloat,profit2Float;
    RelativeLayout adsBanner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_profit_price_activity);

        amountText = findViewById(R.id.amountText);
        buyPriceText = findViewById(R.id.buyPriceText);
        profitText = findViewById(R.id.profitText);


        submitButton = findViewById(R.id.submitButton);
        refreshBtn = findViewById(R.id.refreshBtn);

        sellPriceText = findViewById(R.id.sellPriceText);
        profitText2 = findViewById(R.id.profitText2);
        adsBanner = findViewById(R.id.adsBanner);
        isAdsConfig.callBanner(this,adsBanner);
        Toolbar toolbar = findViewById(R.id.toolbar);

        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Spot Profit Price");
            }
        } catch (Exception e) {
            Log.i("adslog", "exception : " + e.getMessage());
        }
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountText.getText().clear();
                buyPriceText.getText().clear();
                profitText.getText().clear();
                sellPriceText.getText().clear();
                profitText2.getText().clear();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountText.getText().toString().equals("") || buyPriceText.getText().toString().equals("")
                        || profitText.getText().toString().equals("")) {
                    Snackbar.make(findViewById(R.id.main_content), "Please fill the form", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.purple_700)).show();
                } else {
                    amountFloat = Long.parseLong(amountText.getText().toString());
                    buyPriceFloat = Long.parseLong(buyPriceText.getText().toString());
                    profitFloat = Long.parseLong(profitText.getText().toString());
                    sellPricefloat= (profitFloat + (amountFloat * buyPriceFloat)) / amountFloat;
                    profit2Float = ((sellPricefloat - buyPriceFloat) / buyPriceFloat) * 100;



                    if (sellPricefloat < 0) {
                        sellPriceText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
                    }
                    if (profit2Float < 0) {
                        profitText2.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
                    }
                    sellPriceText.setText(String.valueOf(sellPricefloat));
                    profitText2.setText(String.valueOf(profit2Float));
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
