package com.haryo.cryptocalculator.modul;

import java.util.Date;

public class DataCrypto {

    String CoinName;
    float Balance;
    float Riskpercent;
    float EntryPrice;
    float StopLost;
    float TakeProfit;
    float RiskAmount;
    float Leverage;
    Boolean isLong;
    String EntriDate;
    public Boolean getLong() {
        return isLong;
    }

    public void setLong(Boolean aLong) {
        isLong = aLong;
    }
    public float getLeverage() {
        return Leverage;
    }

    public void setLeverage(float leverage) {
        Leverage = leverage;
    }

    public float getBalance() {
        return Balance;
    }

    public void setBalance(float balance) {
        Balance = balance;
    }

    public float getRiskpercent() {
        return Riskpercent;
    }

    public void setRiskpercent(float riskpercent) {
        Riskpercent = riskpercent;
    }

    public DataCrypto(String coinName, float balance, float riskpercent, float entryPrice,
                      float stopLost, float takeProfit, float riskAmount, float leverage,Boolean isLong, String entriDate) {
        CoinName = coinName;
        Balance = balance;
        Riskpercent = riskpercent;
        EntryPrice = entryPrice;
        StopLost = stopLost;
        TakeProfit = takeProfit;
        RiskAmount = riskAmount;
        Leverage = leverage;
        this.isLong = isLong;
        EntriDate = entriDate;
    }

    public void setCoinName(String coinName) {
        CoinName = coinName;
    }

    public String getCoinName() {
        return CoinName;
    }

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

    public String getEntriDate() {
        return EntriDate;
    }

    public void setEntriDate(String entriDate) {
        EntriDate = entriDate;
    }

}
