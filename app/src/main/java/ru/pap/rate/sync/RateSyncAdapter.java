package ru.pap.rate.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;

import ru.pap.rate.UserPreferences;
import ru.pap.rate.service.LoadDataService;
import ru.pap.rate.service.QuoteSyncParam;
import ru.pap.rate.service.SymbolSyncParam;
import ru.pap.rate.service.LoadDataHelper;
import ru.pap.rate.widget.SymbolQuoteWidget;

/**
 * Created by alex on 14.11.16.
 */

public class RateSyncAdapter extends AbstractThreadedSyncAdapter {


    public static String SYNC_TYPE_PARAM = "sync_type_param";

    public RateSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public RateSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient, SyncResult syncResult) {
        String syncTypeName = bundle.getString(SYNC_TYPE_PARAM, SyncType.ALL.name());
        SyncType syncType = SyncType.valueOf(syncTypeName);
        switch (syncType) {
            case NONE:
                break;
            case QUOTE:
                onSyncQuote();
                break;
            case SYMBOL:
                onSyncSymbol();
                break;
            case ALL:
            default:
                onSyncAll();
                break;
        }
    }

    private void onSyncAll() {
        onSyncSymbol();
        onSyncQuote();
    }

    private void onSyncSymbol() {
        SymbolSyncParam symbolSyncParam = new SymbolSyncParam();
        symbolSyncParam.setRefresh(false);
        Intent intent = new Intent(getContext(), LoadDataService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(LoadDataHelper.QUERY_PARAMS, symbolSyncParam);
        intent.putExtra(LoadDataService.DATA_PARAM, bundle);
        getContext().startService(intent);
    }

    private void onSyncQuote() {
        String currentSymbol = UserPreferences.getPreferences().getCurrentSymbol();
        if (TextUtils.isEmpty(currentSymbol)) {
            return;
        }
        QuoteSyncParam quoteSyncParam = new QuoteSyncParam(currentSymbol);
        quoteSyncParam.setStateSync(QuoteSyncParam.StateSync.SYNC);
        Bundle bundle = new Bundle();
        bundle.putSerializable(LoadDataHelper.QUERY_PARAMS, quoteSyncParam);
        Intent intent = new Intent(getContext(), LoadDataService.class);
        intent.putExtra(LoadDataService.DATA_PARAM, bundle);
        getContext().startService(intent);
    }
}
