package ru.pap.rate.service;

import ru.pap.rate.Config;
import ru.pap.rate.db.QuoteContract;

/**
 * Created by alex on 11.11.16.
 */

public class QuoteSyncParam extends BaseSyncParam {



    public enum StateSync {
        SYNC,
        UP,
        DOWN;
    }

    private int mLimit = Config.DEFAULT_LOAD_ELEMENT_LIMIT;
    private int mOffset;
    private String mSymbol;
    private StateSync mStateSync = StateSync.DOWN;


    public QuoteSyncParam(String symbol) {
        super(QuoteContract.TABLE);
        mSymbol = symbol;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public int getLimit() {
        return mLimit;
    }

    public void setLimit(int limit) {
        mLimit = limit;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        mOffset = offset;
    }

    public StateSync getStateSync() {
        return mStateSync;
    }

    public void setStateSync(StateSync stateSync) {
        mStateSync = stateSync;
    }
}
