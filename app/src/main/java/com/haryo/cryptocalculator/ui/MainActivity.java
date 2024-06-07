package com.haryo.cryptocalculator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.haryo.cryptocalculator.R;
import com.haryo.cryptocalculator.isConfig.SharedPreference;
import com.haryo.cryptocalculator.modul.DataCrypto;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int percent = 0;
    int balanceInt = 0;
    int riskAmountInt = 0;
    MaterialButton btnSubmit;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    LinearLayout barisResult, resultList;
    TextInputEditText balance, riskAmount, entryPrice, stopLoss, leverage, riskPercent, takeProfit, coinName;
    TextInputEditText riskRewardText, posSizeCoinText, posSizeUsdtText, roeUsdtText, pnlUsdtText;
    RadioButton longSelect, shortSelect;
    RadioGroup barislima;
    MaterialButton submitButton,resetButton;
    SharedPreference sharedPref;

    int position = -1;
    Boolean isLong = true;

    @Override
    public void onSaveInstanceState(Bundle saveInsBundleState) {
        super.onSaveInstanceState(saveInsBundleState);
        saveInsBundleState.putInt("pos", position);

    }

    DataCrypto dataCrypto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.main_activity);
        sharedPref = new SharedPreference();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("pos");
            dataCrypto = sharedPref.getCoins(this).get(position);
        } else {
            if (savedInstanceState != null) {
                position = savedInstanceState.getInt("pos");
                Log.i("adslog", "onCreate: pos " + position);
//                dataCrypto = sharedPref.getCoins(this).get(position);
            } else {
                dataCrypto = null;
                position = -1;
            }
        }


        barisResult = findViewById(R.id.barisResult);
        resultList = findViewById(R.id.resultList);
        barislima = findViewById(R.id.barislima);
        submitButton = findViewById(R.id.submitButton);

        coinName = findViewById(R.id.coinNameText);
        balance = findViewById(R.id.balanceText);
        riskPercent = findViewById(R.id.riskPercentText);
        riskAmount = findViewById(R.id.riskAmountText);
        entryPrice = findViewById(R.id.entryPriceText);
        takeProfit = findViewById(R.id.takeProfitText);
        stopLoss = findViewById(R.id.stopLostText);
        leverage = findViewById(R.id.leverageText);

        takeProfit.setFilters(new InputFilter[] { filter });
        stopLoss.setFilters(new InputFilter[] { filter });
        leverage.setFilters(new InputFilter[] { filter });
        riskPercent.setFilters(new InputFilter[] { filter });
        riskAmount.setFilters(new InputFilter[] { filter });
        balance.setFilters(new InputFilter[] { filter });
        entryPrice.setFilters(new InputFilter[] { filter });


        riskRewardText = findViewById(R.id.riskReward);
        posSizeCoinText = findViewById(R.id.posSizeCoin);
        posSizeUsdtText = findViewById(R.id.posSizeUsdt);
        roeUsdtText = findViewById(R.id.roeUsdt);
        pnlUsdtText = findViewById(R.id.pnlUsdt);

        longSelect = findViewById(R.id.longSelect);
        shortSelect = findViewById(R.id.shortSelect);
        btnSubmit = findViewById(R.id.submitButton);
        resetButton = findViewById(R.id.resetButton);

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.main_content);
        Toolbar toolbar = findViewById(R.id.toolbar);

        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayShowTitleEnabled(true);
        } catch (Exception e) {
            Log.i("adslog", "exception : " + e.getMessage());
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.material_drawer_open, R.string.material_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        barislima.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);

        barisResult.setVisibility(View.GONE);
        resultList.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barislima.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.VISIBLE);

                barisResult.setVisibility(View.GONE);
                resultList.setVisibility(View.GONE);
                resetButton.setVisibility(View.GONE);
                clearAll();
            }
        });
        if (dataCrypto != null) {
            DataCrypto data = sharedPref.getCoins(this).get(position);
            coinName.setText(new String(data.getCoinName()));
            balance.setText(new String(String.valueOf(data.getBalance())));
            riskPercent.setText(new String(String.valueOf(data.getRiskpercent())));
            entryPrice.setText(new String(String.valueOf(data.getEntryPrice())));
            takeProfit.setText(new String(String.valueOf(data.getTakeProfit())));
            stopLoss.setText(new String(String.valueOf(data.getStopLost())));
            leverage.setText(new String(String.valueOf(data.getLeverage())));

            barislima.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
            resetButton.setVisibility(View.VISIBLE);

            barisResult.setVisibility(View.VISIBLE);
            resultList.setVisibility(View.VISIBLE);

            balanceFloat = data.getBalance();
            riskPercentFloat = data.getRiskpercent();
            riskAmountFloat = data.getRiskAmount();
            entryPriceFloat = data.getEntryPrice();
            takeProfitFloat = data.getTakeProfit();
            stopLossFloat = data.getStopLost();
            leverageFloat = data.getLeverage();



            hitungPertama();
            hitungKedua(data.getLong());
            riskRewardText.setText(String.format(Locale.US, "%.8f", riskReward));
            posSizeCoinText.setText(String.format(Locale.US, "%.8f", positionSizeCoin));
            posSizeUsdtText.setText(String.format(Locale.US, "%.8f", posSizeUsdt));

            roeUsdtText.setText(String.format(Locale.US, "%.8f", roeFloat));
            pnlUsdtText.setText(String.format(Locale.US, "%.8f", pnlUsdt));

        }
        btnSubmit.setOnClickListener(v -> {
            if (balance.getText().toString().equals("") || riskPercent.getText().toString().equals("") || entryPrice.getText().toString().equals("")
                    || takeProfit.getText().toString().equals("") || stopLoss.getText().toString().equals("") || leverage.getText().toString().equals("")) {
                Snackbar.make(findViewById(R.id.main_content), "Please fill the form", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.purple_700)).show();
            } else {
                cekEntry();
                hitungKedua(longSelect.isChecked());

                String coinNametext = coinName.getText().toString();
                riskRewardText.setText(String.format(Locale.US, "%.8f", riskReward));
                posSizeCoinText.setText(String.format(Locale.US, "%.8f", positionSizeCoin));
                posSizeUsdtText.setText(String.format(Locale.US, "%.8f", posSizeUsdt));

                roeUsdtText.setText(String.format(Locale.US, "%.8f", roeFloat));
                pnlUsdtText.setText(String.format(Locale.US, "%.8f", pnlUsdt));

                DataCrypto developers = new DataCrypto(coinNametext, balanceFloat, riskPercentFloat
                        , entryPriceFloat, stopLossFloat,
                        takeProfitFloat, riskAmountFloat, leverageFloat, isLong, getDateNow());
                sharedPref.addCoin(this, developers);


                barislima.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);
                resetButton.setVisibility(View.VISIBLE);

                barisResult.setVisibility(View.VISIBLE);
                resultList.setVisibility(View.VISIBLE);

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

    private void clearAll() {
        coinName.getText().clear();
        balance.getText().clear();
        riskPercent.getText().clear();
        entryPrice.getText().clear();
        takeProfit.getText().clear();
        stopLoss.getText().clear();
        leverage.getText().clear();
    }

    private void hitungKedua(boolean checked) {
        if (checked) {
            roeFloat = ((takeProfitFloat - entryPriceFloat) / (entryPriceFloat) * 100 * leverageFloat);
            pnlUsdt = (posSizeUsdt * roeFloat) / 100;
            isLong = true;
        } else {
            roeFloat = ((takeProfitFloat - entryPriceFloat) / (entryPriceFloat) * 100 * leverageFloat * -1);
            pnlUsdt = (posSizeUsdt * roeFloat) / 100;
            isLong = false;
        }
    }


    float leverageFloat, balanceFloat, riskPercentFloat, riskAmountFloat,
            takeProfitFloat, stopLossFloat, entryPriceFloat,
            riskReward, positionSizeCoin, posSizeUsdt, roeFloat, pnlUsdt;

    private void cekEntry() {
        String balanceText = balance.getText().toString();
        String riskPercentText = riskPercent.getText().toString();
        String riskAmountText = riskAmount.getText().toString();
        String entryText = entryPrice.getText().toString();
        String takeProfitText = takeProfit.getText().toString();
        String stopLossText = stopLoss.getText().toString();
        String leverageText = leverage.getText().toString();
        String dateNow = getDateNow();

        balanceFloat = Float.parseFloat(balanceText);
        riskPercentFloat = Float.parseFloat(riskPercentText);
        riskAmountFloat = Float.parseFloat(riskAmountText);
        entryPriceFloat = Float.parseFloat(entryText);
        takeProfitFloat = Float.parseFloat(takeProfitText);
        stopLossFloat = Float.parseFloat(stopLossText);
        leverageFloat = Float.parseFloat(leverageText);

        Log.w("adslog", "date now :  " + dateNow);
        hitungPertama();
    }

    private void hitungPertama() {
        riskReward = (takeProfitFloat - entryPriceFloat) / (entryPriceFloat - stopLossFloat);
        positionSizeCoin = leverageFloat * ((riskAmountFloat * entryPriceFloat) / (leverageFloat * (stopLossFloat * entryPriceFloat)) / entryPriceFloat) * -1;
        posSizeUsdt = (entryPriceFloat * positionSizeCoin) / leverageFloat;
    }

    private String getDateNow() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.i("adslog", "onNavigationItemSelected: " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.fut_pos:
                break;
            case R.id.spot_pos_size:
                Intent spot = new Intent(MainActivity.this, SpotPosSizeActivity.class);
                startActivity(spot);

                break;
            case R.id.fut_pnl:
                Intent fut = new Intent(MainActivity.this, FuturePnlActivity.class);
                startActivity(fut);
                break;
            case R.id.history:
                Intent his = new Intent(MainActivity.this, CoinHistory.class);
                startActivity(his);
                finish();
                break;
            case R.id.fut_target_price:
                Intent ftp = new Intent(MainActivity.this, FutureTargPrice.class);
                startActivity(ftp);
                finish();
                break;
                case R.id.tips:
                Intent usage = new Intent(MainActivity.this, UsageActivity.class);
                startActivity(usage);
                finish();
                break;
                case R.id.spot_profit:
                Intent spot_profit = new Intent(MainActivity.this, SpotProfitActivity.class);
                startActivity(spot_profit);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };
}
