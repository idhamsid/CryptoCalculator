package com.haryo.cryptocalculator.isConfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.haryo.cryptocalculator.modul.DataCrypto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {

    public static final String PREFS_NAME = "CRYPTO_APP";
    public static final String FAVORITES = "crypto_calc";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<DataCrypto> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addCoin(Context context, DataCrypto product) {
        List<DataCrypto> favorites = getCoins(context);
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, DataCrypto product) {
        ArrayList<DataCrypto> favorites = getCoins(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<DataCrypto> getCoins(Context context) {
        SharedPreferences settings;
        List<DataCrypto> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            DataCrypto[] favoriteItems = gson.fromJson(jsonFavorites,
                    DataCrypto[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<DataCrypto>) favorites;
    }
}
