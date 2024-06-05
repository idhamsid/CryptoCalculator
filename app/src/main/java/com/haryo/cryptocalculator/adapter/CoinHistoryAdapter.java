package com.haryo.cryptocalculator.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.haryo.cryptocalculator.R;
import com.haryo.cryptocalculator.modul.DataCrypto;
import com.haryo.cryptocalculator.ui.MainActivity;

import java.util.ArrayList;

public class CoinHistoryAdapter extends RecyclerView.Adapter {

    static ArrayList<DataCrypto> coinLists;
    public Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public MaterialButton buttonFuturePos;
        public MaterialButton buttonDetail;
        public TextView coinName;
        public TextView entryPrice;
        public TextView stopLoss;
        public TextView takeProfit;
        public TextView riskAmount;
        public TextView dateSubmit;
        public ViewHolder(View itemView) {
            super(itemView);
            buttonFuturePos = itemView.findViewById(R.id.button);
            buttonDetail = itemView.findViewById(R.id.buttonDetail);

            coinName = itemView.findViewById(R.id.coinName);
            entryPrice = itemView.findViewById(R.id.entryPrice);
            stopLoss = itemView.findViewById(R.id.stopLoss);
            takeProfit = itemView.findViewById(R.id.takeProfit);
            riskAmount = itemView.findViewById(R.id.riskAmount);
            dateSubmit = itemView.findViewById(R.id.dateSubmit);
        }

    }


    public CoinHistoryAdapter(ArrayList<DataCrypto> coinListss, Context context) {
        coinLists = coinListss;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof ViewHolder) {
            final DataCrypto itemList = coinLists.get(position);
            ((ViewHolder) holder).coinName.setText(itemList.getCoinName()+"/USDT");
            ((ViewHolder) holder).entryPrice.setText("Entry price : "+String.valueOf(itemList.getEntryPrice()));
            ((ViewHolder) holder).stopLoss.setText("Stop lost : "+String.valueOf(itemList.getStopLost()));
            ((ViewHolder) holder).takeProfit.setText("Take profit : "+String.valueOf(itemList.getTakeProfit()));
            ((ViewHolder) holder).riskAmount.setText("Risk amount : "+String.valueOf(itemList.getRiskAmount()));
            ((ViewHolder) holder).dateSubmit.setText("Entry date : "+itemList.getEntriDate());

            ((ViewHolder) holder).buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent futurePos = new Intent(context, MainActivity.class);
                    futurePos.putExtra("pos",position);
                    context.startActivity(futurePos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return coinLists.size();
    }
}
