package ru.pap.rate.model;


import java.util.Date;

/**
 * Created by alex on 09.11.16.
 */

public class Symbol extends BaseModel {

    private String name;
    private double price;
    private String symbol;
    private long ts;
    private String type;
    private Date utctime;
    private long volume;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUtctime() {
        return utctime;
    }

    public void setUtctime(Date utctime) {
        this.utctime = utctime;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
}
