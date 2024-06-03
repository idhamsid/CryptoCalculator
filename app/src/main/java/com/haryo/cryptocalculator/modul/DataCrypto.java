package com.haryo.cryptocalculator.modul;

import java.util.Date;

public class DataCrypto {
    float EntryPrice;
    float StopLost;
    float TakeProfit;
    float RiskAmount;
    Date EntriDate;

    public float getEntryPrice() {
        return EntryPrice;
    }

    public void setEntryPrice(float entryPrice) {
        EntryPrice = entryPrice;
    }

    public float getStopLost() {
        return StopLost;
    }

    public void setStopLost(float stopLost) {
        StopLost = stopLost;
    }

    public float getTakeProfit() {
        return TakeProfit;
    }

    public void setTakeProfit(float takeProfit) {
        TakeProfit = takeProfit;
    }

    public float getRiskAmount() {
        return RiskAmount;
    }

    public void setRiskAmount(float riskAmount) {
        RiskAmount = riskAmount;
    }

    public Date getEntriDate() {
        return EntriDate;
    }

    public void setEntriDate(Date entriDate) {
        EntriDate = entriDate;
    }

}
