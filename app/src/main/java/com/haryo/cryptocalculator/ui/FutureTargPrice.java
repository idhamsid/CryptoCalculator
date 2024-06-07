package com.haryo.cryptocalculator.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.haryo.cryptocalculator.R;

public class FutureTargPrice extends AppCompatActivity {
    TextInputEditText roeText, exitPrice, entryPrice, leverage,targetPriceText;
    RadioButton longSelect, shortSelect;
    RadioGroup barislima;
    MaterialButton submitButton,refreshBtn;
    float leverageFloat,
            entryPriceFloat,
            exitPriceFloat,
            roeFloat,
            targetPriceFloat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.future_target_price);

        barislima = findViewById(R.id.barislima);

        entryPrice = findViewById(R.id.entryPriceText);
        exitPrice = findViewById(R.id.exitPrice);
        roeText = findViewById(R.id.roeText);
        leverage = findViewById(R.id.leverage);
        targetPriceText = findViewById(R.id.targetPriceText);

        longSelect = findViewById(R.id.longSelect);
        shortSelect = findViewById(R.id.shortSelect);
        submitButton = findViewById(R.id.submitButton);
        refreshBtn = findViewById(R.id.refreshBtn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Future Target Price");
            }
        } catch (Exception e) {
            Log.i("adslog", "exception : " + e.getMessage());
        }

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entryPrice.getText().toString().equals("") || exitPrice.getText().toString().equals("")
                        || roeText.getText().toString().equals("") || leverage.getText().toString().equals("") ) {
                    Snackbar.make(findViewById(R.id.main_content), "Please fill the form", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.purple_700)).show();
                } else {
                    cekEntry();
                    if (longSelect.isChecked()) {
                        targetPriceFloat = entryPriceFloat + ((entryPriceFloat*roeFloat/100)/leverageFloat);
                    }else {
                        targetPriceFloat = entryPriceFloat - ((entryPriceFloat*roeFloat/100)/leverageFloat);
                    }
                    targetPriceText.setText(String.valueOf(targetPriceFloat));
                }
            }
        });
    }

    private void cekEntry() {

        String entryText = entryPrice.getText().toString();
        entryPriceFloat = Float.parseFloat(entryText);
        String exitText = exitPrice.getText().toString();
        exitPriceFloat = Float.parseFloat(exitText);
        String leverageText = leverage.getText().toString();
        leverageFloat = Float.parseFloat(leverageText);
        String roeTxt = roeText.getText().toString();
        roeFloat =  Float.parseFloat(roeTxt);

    }
    private void clearAll() {
        entryPrice.getText().clear();
        exitPrice.getText().clear();
        roeText.getText().clear();
        leverage.getText().clear();
        targetPriceText.getText().clear();
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
