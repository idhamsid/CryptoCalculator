package com.haryo.cryptocalculator.ui;

import static com.haryo.cryptocalculator.isConfig.Settings.LAMA_LOAD_ADS;
import static com.haryo.cryptocalculator.isConfig.isAdsConfig.nativeAd;
import static com.haryo.cryptocalculator.isConfig.isAdsConfig.nativeAdLoader;
import static com.haryo.cryptocalculator.isConfig.isAdsConfig.nativeAdMax;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.haryo.cryptocalculator.BuildConfig;
import com.haryo.cryptocalculator.R;
import com.haryo.cryptocalculator.isConfig.SharedPreference;
import com.haryo.cryptocalculator.isConfig.isAdsConfig;
import com.haryo.cryptocalculator.modul.DataCrypto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    float percent = 0;
    float balanceInt = 0;
    float riskAmountInt = 0;
    MaterialButton btnSubmit;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    LinearLayout barisResult, resultList;
    TextInputEditText balance, riskAmount, entryPrice, stopLoss, leverage, riskPercent, takeProfit, coinName;
    TextInputLayout entryPriceLay, takeProfitLay, stopLost, posSizeCoinName, posSizeUsdtName;
    TextInputEditText riskRewardText, posSizeCoinText, posSizeUsdtText, roeUsdtText, pnlUsdtText;
    RadioButton longSelect, shortSelect;
    RadioGroup barislima;
    MaterialButton submitButton, resetButton;
    SharedPreference sharedPref;

    int position = -1;
    Boolean isLong = true;
    RelativeLayout adsBanner;

    @Override
    public void onSaveInstanceState(Bundle saveInsBundleState) {
        super.onSaveInstanceState(saveInsBundleState);
        saveInsBundleState.putInt("pos", position);

    }

    DataCrypto dataCrypto;

    @Override
    public void onBackPressed() {
        dialogExit();
    }

    void dialogExit() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.SheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_open);

        Button tbOpen = dialog.findViewById(R.id.tbYes);
        tbOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        isAdsConfig.callNative(MainActivity.this, dialog.findViewById(R.id.layAds), R.layout.admob_native_big, R.layout.max_big_native);
        Button open = dialog.findViewById(R.id.tbRate);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="
                        + BuildConfig.APPLICATION_ID)));
            }
        });
        ImageButton tbClose = dialog.findViewById(R.id.imgExit);
        tbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    ArrayList<DataCrypto> coinListsM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        sharedPref = new SharedPreference();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("pos");
            int positionReverse = sharedPref.getCoins(this).size() - (position+1);
            dataCrypto = sharedPref.getCoins(this).get(positionReverse);
        } else {
            if (savedInstanceState != null) {
                position = savedInstanceState.getInt("pos");
                Log.i("adslog", "onCreate: pos " + position);
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


        riskRewardText = findViewById(R.id.riskReward);
        posSizeCoinText = findViewById(R.id.posSizeCoin);
        posSizeUsdtText = findViewById(R.id.posSizeUsdt);
        roeUsdtText = findViewById(R.id.roeUsdt);
        pnlUsdtText = findViewById(R.id.pnlUsdt);

        longSelect = findViewById(R.id.longSelect);
        shortSelect = findViewById(R.id.shortSelect);
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

        adsBanner = findViewById(R.id.adsBanner);
        isAdsConfig.loadInters(this, false);
        isAdsConfig.callBanner(MainActivity.this, adsBanner);


        entryPriceLay = findViewById(R.id.entryPrice);
        takeProfitLay = findViewById(R.id.takeProfit);
        stopLost = findViewById(R.id.stopLost);
        posSizeCoinName = findViewById(R.id.posSizeCoinName);
        posSizeUsdtName = findViewById(R.id.posSizeUsdtName);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        entryPriceLay.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText(getString(R.string.app_name), entryPrice.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied to clipboard !", Toast.LENGTH_SHORT).show();
            }
        });
        takeProfitLay.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText(getString(R.string.app_name), takeProfit.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied to clipboard !", Toast.LENGTH_SHORT).show();

            }
        });
        stopLost.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText(getString(R.string.app_name), stopLoss.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied to clipboard !", Toast.LENGTH_SHORT).show();

            }
        });
        posSizeCoinName.setOnClickListener(v -> {
            ClipData clip = ClipData.newPlainText(getString(R.string.app_name), posSizeUsdtText.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(MainActivity.this, "Copied to clipboard !", Toast.LENGTH_SHORT).show();
        });
        posSizeCoinName.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText(getString(R.string.app_name), posSizeCoinText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied to clipboard !", Toast.LENGTH_SHORT).show();

            }
        });
        posSizeUsdtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText(getString(R.string.app_name), posSizeUsdtText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied to clipboard !", Toast.LENGTH_SHORT).show();
            }
        });
        posSizeUsdtName.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText(getString(R.string.app_name), posSizeUsdtText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied to clipboard !", Toast.LENGTH_SHORT).show();
            }
        });
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
            coinName.setText(new String(dataCrypto.getCoinName()));
            balance.setText(new String(String.valueOf(dataCrypto.getBalance())));
            riskPercent.setText(new String(String.valueOf(dataCrypto.getRiskpercent())));
            entryPrice.setText(new String(String.valueOf(dataCrypto.getEntryPrice())));
            takeProfit.setText(new String(String.valueOf(dataCrypto.getTakeProfit())));
            stopLoss.setText(new String(String.valueOf(dataCrypto.getStopLost())));
            leverage.setText(new String(String.valueOf(dataCrypto.getLeverage())));

            barislima.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
            resetButton.setVisibility(View.VISIBLE);

            barisResult.setVisibility(View.VISIBLE);
            resultList.setVisibility(View.VISIBLE);

            balanceFloat = dataCrypto.getBalance();
            riskPercentFloat = dataCrypto.getRiskpercent();
            riskAmountFloat = dataCrypto.getRiskAmount();
            entryPriceFloat = dataCrypto.getEntryPrice();
            takeProfitFloat = dataCrypto.getTakeProfit();
            stopLossFloat = dataCrypto.getStopLost();
            leverageFloat = dataCrypto.getLeverage();


            hitungPertama();
            hitungKedua(dataCrypto.getLong());


            if (riskReward < 0) {
                riskRewardText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
            }
            if (positionSizeCoin < 0) {
                posSizeCoinText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
            }
            if (posSizeUsdt < 0) {
                posSizeUsdtText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
            }
            if (roeFloat < 0) {
                roeUsdtText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
            }
            if (pnlUsdt < 0) {
                pnlUsdtText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
            }

            riskRewardText.setText(String.format(Locale.US, "%.8f", riskReward));
            posSizeCoinText.setText(String.format(Locale.US, "%.8f", positionSizeCoin));
            posSizeUsdtText.setText(String.format(Locale.US, "%.8f", posSizeUsdt));

            roeUsdtText.setText(String.format(Locale.US, "%.8f", roeFloat));
            pnlUsdtText.setText(String.format(Locale.US, "%.8f", pnlUsdt));

        }
        submitButton.setOnClickListener(v -> {
            if (balance.getText().toString().equals("") || riskPercent.getText().toString().equals("") || entryPrice.getText().toString().equals("")
                    || takeProfit.getText().toString().equals("") || stopLoss.getText().toString().equals("") || leverage.getText().toString().equals("")) {
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
                isAdsConfig.showInterst(MainActivity.this, true, LAMA_LOAD_ADS);
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
                    balanceInt = Float.parseFloat(new String(s.toString()));
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
                    percent = Float.parseFloat(new String(s.toString()));
                if (balanceInt != 0) {
                    riskAmountInt = balanceInt * percent / 100;
                    riskAmount.setText(String.valueOf(riskAmountInt));
                } else
                    riskAmount.setText(new String(s.toString()));

            }
        });
    }

    private void proses() {
        cekEntry();
        hitungKedua(longSelect.isChecked());
        String coinNametext = coinName.getText().toString();
        if (riskReward < 0) {
            riskRewardText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
        }
        if (positionSizeCoin < 0) {
            posSizeCoinText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
        }
        if (posSizeUsdt < 0) {
            posSizeUsdtText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
        }
        if (roeFloat < 0) {
            roeUsdtText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
        }
        if (pnlUsdt < 0) {
            pnlUsdtText.setTextColor(getResources().getColor(R.color.color_youtube_red_light));
        }
        riskRewardText.setText(String.format(Locale.US, "%.8f", riskReward));
        posSizeCoinText.setText(String.format(Locale.US, "%.8f", positionSizeCoin));
        posSizeUsdtText.setText(String.format(Locale.US, "%.8f", posSizeUsdt));

        roeUsdtText.setText(String.format(Locale.US, "%.8f", roeFloat));
        pnlUsdtText.setText(String.format(Locale.US, "%.8f", pnlUsdt));

        DataCrypto developers = new DataCrypto(coinNametext, balanceFloat, riskPercentFloat
                , entryPriceFloat, stopLossFloat,
                takeProfitFloat, riskAmountFloat, leverageFloat, isLong, getDateNow(), "Future Position");
        sharedPref.addCoin(this, developers);


        barislima.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
        resetButton.setVisibility(View.VISIBLE);

        barisResult.setVisibility(View.VISIBLE);
        resultList.setVisibility(View.VISIBLE);


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
            case R.id.spot_pos_size:
                Intent spot = new Intent(MainActivity.this, SpotPosSizeActivity.class);
                isAdsConfig.setIsAdsListener(new isAdsConfig.IsAdsListener() {
                    @Override
                    public void onClose() {
                        startActivity(spot);
                    }

                    @Override
                    public void onShow() {
                    }

                    @Override
                    public void onNotShow() {
                        startActivity(spot);
                    }
                });
                isAdsConfig.showInterst(MainActivity.this, true, LAMA_LOAD_ADS);
                break;
            case R.id.fut_pnl:
                Intent fut = new Intent(MainActivity.this, FuturePnlActivity.class);
                isAdsConfig.setIsAdsListener(new isAdsConfig.IsAdsListener() {
                    @Override
                    public void onClose() {
                        startActivity(fut);
                    }

                    @Override
                    public void onShow() {
                    }

                    @Override
                    public void onNotShow() {
                        startActivity(fut);
                    }
                });
                isAdsConfig.showInterst(MainActivity.this, true, LAMA_LOAD_ADS);
                break;
            case R.id.history:
                Intent his = new Intent(MainActivity.this, CoinHistory.class);
                startActivity(his);
                break;
            case R.id.fut_target_price:
                Intent ftp = new Intent(MainActivity.this, FutureTargPrice.class);
                isAdsConfig.setIsAdsListener(new isAdsConfig.IsAdsListener() {
                    @Override
                    public void onClose() {
                        startActivity(ftp);
                    }

                    @Override
                    public void onShow() {
                    }

                    @Override
                    public void onNotShow() {
                        startActivity(ftp);
                    }
                });
                isAdsConfig.showInterst(MainActivity.this, true, LAMA_LOAD_ADS);
                break;
            case R.id.tips:
                Intent usage = new Intent(MainActivity.this, UsageActivity.class);
                startActivity(usage);
                break;
            case R.id.spot_profit:
                Intent spot_profit = new Intent(MainActivity.this, SpotProfitActivity.class);
                isAdsConfig.setIsAdsListener(new isAdsConfig.IsAdsListener() {
                    @Override
                    public void onClose() {
                        startActivity(spot_profit);
                    }

                    @Override
                    public void onShow() {
                    }

                    @Override
                    public void onNotShow() {
                        startActivity(spot_profit);
                    }
                });
                isAdsConfig.showInterst(MainActivity.this, true, LAMA_LOAD_ADS);
                break;
            case R.id.spot_provit_price:
                Intent spot_provit_price = new Intent(MainActivity.this, SpotProfitPriceActivity.class);
                isAdsConfig.setIsAdsListener(new isAdsConfig.IsAdsListener() {
                    @Override
                    public void onClose() {
                        startActivity(spot_provit_price);
                    }

                    @Override
                    public void onShow() {
                    }

                    @Override
                    public void onNotShow() {
                        startActivity(spot_provit_price);
                    }
                });
                isAdsConfig.showInterst(MainActivity.this, true, LAMA_LOAD_ADS);
                break;
            case R.id.about:
                showAbout();
                break;
            case R.id.share:
                share();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + "\n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        intent.setType("text/plain");
        startActivity(intent);
    }

    private void showAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.message_about))
                .setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
