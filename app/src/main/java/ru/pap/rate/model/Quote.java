package ru.pap.rate.model;

import com.google.gson.annotations.SerializedName;

import android.text.TextUtils;

import java.util.Date;

/**
 * Created by alex on 09.11.16.
 */

public class Quote extends BaseModel {

    private String name;

    @SerializedName("Symbol")
    private String symbol;
    @SerializedName("Date")
    private Date date;
    @SerializedName("Open")
    private double open;
    @SerializedName("High")
    private double high;
    @SerializedName("Low")
    private double low;
    @SerializedName("Close")
    private double close;
    @SerializedName("Volume")
    private String volume;
    @SerializedName("Adj_Close")
    private double adjClose;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public double getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(double adjClose) {
        this.adjClose = adjClose;
    }

    //    "Symbol": "RUB%3dX",
//            "Date": "2010-03-10",
//            "Open": "29.7889",
//            "High": "29.7889",
//            "Low": "29.51",
//            "Close": "29.5898",
//            "Volume": "000",
//            "Adj_Close": "29.5898"
}
