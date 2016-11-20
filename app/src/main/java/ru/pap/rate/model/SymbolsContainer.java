package ru.pap.rate.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 10.11.16.
 */

public class SymbolsContainer extends BaseModel {

    @SerializedName("resources")
    private List<Symbol> mSymbols;

    public List<Symbol> getSymbols() {
        if (mSymbols == null) {
            mSymbols = new ArrayList<>();
        }
        return mSymbols;
    }

    public void setSymbols(List<Symbol> symbols) {
        mSymbols = symbols;
    }
}
