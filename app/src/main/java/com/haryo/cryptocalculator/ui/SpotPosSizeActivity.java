package com.haryo.cryptocalculator.ui;

import static com.haryo.cryptocalculator.isConfig.Settings.LAMA_LOAD_ADS;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.haryo.cryptocalculator.R;
import com.haryo.cryptocalculator.isConfig.isAdsConfig;

public class SpotPosSizeActivity extends AppCompatActivity {
    TextInputEditText balance, riskAmount, entryPrice, stopLoss,
            takeProfitText, riskPercent, feeText, coinNameText;
    TextInputEditText riskReward, posSizeCoin, posSizeUsdt, roeUsdt, pnlUsdt;
    int percent = 0;
    int balanceInt = 0;
    int riskAmountInt = 0;
    LinearLayout resultList, barisResult;
    MaterialButton btnSubmit, resetButton;
    float balanceFloat, riskPercentFloat, riskAmountFloat, entryPriceFloat, stopLossFloat, takeProfitFloat, feeFloat;
    float riskRewardFloat, aFloat, posSizeFloat, positionSizeUSDT, roeFloat, pnlUSDT;

    RelativeLayout adsBanner;
    TextInputLayout entryPriceLay,takeProfit,stopLostLay,posSizeCoinName,posSizeUsdtName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.spot_pos_activity);
        coinNameText = findViewById(R.id.coinNameText);
        balance = findViewById(R.id.balanceText);
        riskPercent = findViewById(R.id.riskPercentText);
        riskAmount = findViewById(R.id.riskAmountText);

        entryPrice = findViewById(R.id.entryPriceText);
        stopLoss = findViewById(R.id.stopLostText);
        takeProfitText = findViewById(R.id.takeProfitText);
        feeText = findViewById(R.id.feeText);

        btnSubmit = findViewById(R.id.submitButton);

        riskReward = findViewById(R.id.riskReward);
        posSizeCoin = findViewById(R.id.posSizeCoin);
        posSizeUsdt = findViewById(R.id.posSizeUsdt);
        roeUsdt = findViewById(R.id.roeUsdt);
        pnlUsdt = findViewById(R.id.pnlUsdt);
        resultList = findViewById(R.id.resultList);
        barisResult = findViewById(R.id.barisResult);

        resetButton = findViewById(R.id.resetButton);

        resultList.setVisibility(View.GONE);
        barisResult.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.GONE);
        adsBanner = findViewById(R.id.adsBanner);

        entryPriceLay= findViewById(R.id.entryPrice);
        takeProfit= findViewById(R.id.takeProfit);
        stopLostLay= findViewById(R.id.stopLost);
        posSizeCoinName= findViewById(R.id.posSizeCoinName);


        isAdsConfig.loadInters(this, false);
        isAdsConfig.callNative(this, adsBanner, R.layout.admob_native_big, R.layout.max_big_native);
        Toolbar toolbar = findViewById(R.id.toolbar);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        entryPriceLay.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText(getString(R.string.app_name), entryPrice.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SpotPosSizeActivity.this, "Copied to clipboard !", Toast.LENGTH_SHORT).show();
            }
        });
        takeProfit.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText(getString(R.string.app_name), takeProfitText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SpotPosSizeActivity.this, "Copied to clipboard !", Toast.LENGTH_SHORT).show();
            }
        });
        stopLostLay.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText(getString(R.string.app_name), stopLoss.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SpotPosSizeActivity.this, "Copied to clipboard !", Toast.LENGTH_SHORT).show();
            }
        });
        posSizeCoinName.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText(getString(R.string.app_name), posSizeCoin.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SpotPosSizeActivity.this, "Copied to clipboard !", Toast.LENGTH_SHORT).show();
            }
        });
        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                {
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle("Spot Position Size");
                }
            }
        } catch (Exception e) {
            Log.i("adslog", "exception : " + e.getMessage());
        }
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultList.setVisibility(View.GONE);
                barisResult.setVisibility(View.GONE);
                resetButton.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);

                balance.setText("");
                riskPercent.setText("");
                riskAmount.setText("");
                entryPrice.setText("");
                stopLoss.setText("");
            }
        });
        btnSubmit.setOnClickListener(v -> {
            if (balance.getText().toString().equals("") || riskPercent.getText().toString().equals("")) {
                Snackbar.make(findViewById(R.id.main_content), "Please fill the form", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.purple_700)).show();
            } else {
                if (balance.getText().toString().equals("") || riskPercent.getText().toString().equals("") || entryPrice.getText().toString().equals("")
                        || takeProfitText.getText().toString().equals("") || stopLoss.getText().toString().equals("")) {
                    Snackbar.make(findViewById(R.id.main_content), "Please fill the form", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.purple_700)).show();
                } else {
                    isAdsConfig.setIsAdsListener(new isAdsConfig.IsAdsListener() {
                        @Override
                        public void onClose() {
                            proses();
                        }

                        @Override
                        public void onShow() {

                        }

                        @Override
                        public void onNotShow() {
                            proses();
                        }
                    });
                    isAdsConfig.showInterst(this,true,LAMA_LOAD_ADS);
                }
            }
        });
        balance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                balanceInt = 0;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override

            public void afterTextChanged(Editable s) {
                String str = new String(s.toString());
                if (str.equals(""))
                    balanceInt = 0;
                else
                    balanceInt = Integer.parseInt(new String(s.toString()));
                if (percent != 0) {
                    riskAmountInt = balanceInt * percent / 100;
                    riskAmount.setText(String.valueOf(riskAmountInt));
                } else
                    riskAmount.setText(new String(s.toString()));

            }
        });
        riskPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                percent = 0;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = new String(s.toString());
                if (str.equals(""))
                    percent = 0;
                else
                    percent = Integer.parseInt(new String(s.toString()));
                if (balanceInt != 0) {
                    riskAmountInt = balanceInt * percent / 100;
                    riskAmount.setText(String.valueOf(riskAmountInt));
                } else
                    riskAmount.setText(new String(s.toString()));

            }
        });
    }

    private void proses() {
        resultList.setVisibility(View.VISIBLE);
        barisResult.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.GONE);

        balanceFloat = Float.parseFloat(balance.getText().toString());
        riskPercentFloat = Float.parseFloat(riskPercent.getText().toString());
        riskAmountFloat = Float.parseFloat(riskAmount.getText().toString());
        entryPriceFloat = Float.parseFloat(entryPrice.getText().toString());
        takeProfitFloat = Float.parseFloat(takeProfitText.getText().toString());
        stopLossFloat = Float.parseFloat(stopLoss.getText().toString());
        if (feeText.getText().toString().equals("")) {
            feeFloat = 0;
        } else {
            feeFloat = Float.parseFloat(feeText.getText().toString());
        }
        riskRewardFloat = (takeProfitFloat - entryPriceFloat) / (entryPriceFloat - stopLossFloat);
        aFloat = (1 * ((riskAmountFloat * entryPriceFloat) / (1 * (stopLossFloat)))) * -1;
        posSizeFloat = aFloat - (aFloat * feeFloat);
        positionSizeUSDT = entryPriceFloat * posSizeFloat;
        roeFloat = ((takeProfitFloat - entryPriceFloat) / entryPriceFloat) * 100;
        pnlUSDT = (positionSizeUSDT * roeFloat) / 100;

        if (riskRewardFloat < 0) {
            riskReward.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
        }
        if (posSizeFloat < 0) {
            posSizeCoin.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
        }
        if (positionSizeUSDT < 0) {
            posSizeUsdt.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
        }
        if (roeFloat < 0) {
            roeUsdt.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
        }
        if (pnlUSDT < 0) {
            pnlUsdt.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
        }


        riskReward.setText(String.valueOf(riskRewardFloat));
        posSizeCoin.setText(String.valueOf(posSizeFloat));
        posSizeUsdt.setText(String.valueOf(positionSizeUSDT));
        roeUsdt.setText(String.valueOf(roeFloat));
        pnlUsdt.setText(String.valueOf(pnlUSDT));

        isAdsConfig.clearBanner(adsBanner);
        isAdsConfig.callBanner(SpotPosSizeActivity.this, adsBanner);


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
