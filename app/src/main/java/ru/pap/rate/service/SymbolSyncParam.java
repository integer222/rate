package ru.pap.rate.service;

import ru.pap.rate.db.SymbolContract;

/**
 * Created by alex on 11.11.16.
 */

public class SymbolSyncParam extends BaseSyncParam {


    private boolean mRefresh;

    public SymbolSyncParam() {
        super(SymbolContract.TABLE);
    }

    public boolean isRefresh() {
        return mRefresh;
    }

    public void setRefresh(boolean refresh) {
        mRefresh = refresh;
    }

}
