package com.haryo.cryptocalculator.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.haryo.cryptocalculator.R;

public class FuturePnlActivity extends AppCompatActivity {
    TextInputEditText initialMargin, exitPrice, entryPrice, leverage, quantity, roe, pnl;
    MaterialButton calculate,refreshBtn;
    RadioButton longSelect, shortSelect;
    float leverageFloat, initMarginFloat, exitPriceFloat, entryPriceFloat, quantityFloat, roeFloat, pnlFloat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.future_pnl_activity);

        entryPrice = findViewById(R.id.entryPrice);
        exitPrice = findViewById(R.id.exitPrice);
        initialMargin = findViewById(R.id.initialMargin);
        leverage = findViewById(R.id.leverage);
        longSelect = findViewById(R.id.longSelect);
        shortSelect = findViewById(R.id.shortSelect);
        calculate = findViewById(R.id.submitButton);
        refreshBtn = findViewById(R.id.refreshBtn);
        quantity = findViewById(R.id.quantity);
        roe = findViewById(R.id.roe);
        pnl = findViewById(R.id.pnl);

        Toolbar toolbar = findViewById(R.id.toolbar);

        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Future PNL");
            }
        } catch (Exception e) {
            Log.i("adslog", "exception : " + e.getMessage());
        }
        refreshBtn.setOnClickListener(v->{
            clearAll();
        });
        calculate.setOnClickListener(v -> {
            if (entryPrice.getText().toString().equals("") || exitPrice.getText().toString().equals("")
                    || initialMargin.getText().toString().equals("") || leverage.getText().toString().equals("")) {
                Snackbar.make(findViewById(R.id.main_content), "Please fill the form", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.purple_700)).show();
            } else {
                cekEntry();
                if (longSelect.isChecked()) {
                    quantity.setText(String.valueOf(quantityFloat));
                    roe.setText(String.valueOf(roeFloat));
                    pnl.setText(String.valueOf(pnlFloat));

                } else if (shortSelect.isChecked()) {
                    quantity.setText(String.valueOf(quantityFloat));

                    float roeShort = roeFloat * -1;
                    roe.setText(String.valueOf(roeShort));

                    float pnlfloatShort = pnlFloat * -1;
                    pnl.setText(String.valueOf(pnlfloatShort));
                }
            }
        });

        longSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (entryPrice.getText().toString().equals("") || exitPrice.getText().toString().equals("")
                        || initialMargin.getText().toString().equals("") || leverage.getText().toString().equals("")) {
                    return;
                }
                if (isChecked) {
                    cekEntry();
                    quantity.setText(String.valueOf(quantityFloat));
                    roe.setText(String.valueOf(roeFloat));
                    pnl.setText(String.valueOf(pnlFloat));
                }
            }
        });
        shortSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (entryPrice.getText().toString().equals("") || exitPrice.getText().toString().equals("")
                        || initialMargin.getText().toString().equals("") || leverage.getText().toString().equals("")) {
                    return;
                }
                if (isChecked) {
                    cekEntry();
                    quantity.setText(String.valueOf(quantityFloat));
                    float roeShort = roeFloat * -1;
                    roe.setText(String.valueOf(roeShort));
                    float pnlfloatShort = pnlFloat * -1;
                    pnl.setText(String.valueOf(pnlfloatShort));
                }
            }
        });

    }

    private void clearAll() {
        entryPrice.setText("");
        exitPrice.setText("");
        initialMargin.setText("");
        leverage.setText("");
        quantity.setText("");
        roe.setText("");
        pnl.setText("");
    }

    private void cekEntry() {
        String leverageText = leverage.getText().toString();
        leverageFloat = Float.parseFloat(leverageText);
        String initText = initialMargin.getText().toString();
        initMarginFloat = Float.parseFloat(initText);
        String entryText = entryPrice.getText().toString();
        entryPriceFloat = Float.parseFloat(entryText);
        String exitText = exitPrice.getText().toString();
        exitPriceFloat = Float.parseFloat(exitText);
        quantityFloat = leverageFloat * initMarginFloat * entryPriceFloat;
        roeFloat = ((exitPriceFloat - entryPriceFloat) / entryPriceFloat) * 100 * leverageFloat;
        pnlFloat = ((entryPriceFloat * quantityFloat) / leverageFloat) * roeFloat / 100;
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
