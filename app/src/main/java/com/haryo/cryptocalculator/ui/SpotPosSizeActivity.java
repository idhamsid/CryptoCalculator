package com.haryo.cryptocalculator.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.haryo.cryptocalculator.R;

public class SpotPosSizeActivity extends AppCompatActivity {
    TextInputEditText balance, riskAmount, entryPrice, stopLoss, targetPrice, riskPercent;
    int percent = 0;
    int balanceInt = 0;
    int riskAmountInt = 0;
    MaterialButton btnSubmit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.spot_pos_activity);

        balance = findViewById(R.id.balanceText);
        riskPercent = findViewById(R.id.riskPercentText);
        riskAmount = findViewById(R.id.riskAmountText);
        btnSubmit = findViewById(R.id.submitButton);

        Toolbar toolbar = findViewById(R.id.toolbar);

        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                {
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle("Spot Position Size");
                }
            }
            } catch(Exception e){
                Log.i("adslog", "exception : " + e.getMessage());
            }

        btnSubmit.setOnClickListener(v->{
            if(balance.getText().toString().equals("") || riskPercent.getText().toString().equals("")){
                Snackbar.make(findViewById(R.id.main_content),"Please fill the form",Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.purple_700)).show();
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

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    break;
            }
            return true;
        }

    }
