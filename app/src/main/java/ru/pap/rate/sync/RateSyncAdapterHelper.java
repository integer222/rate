package ru.pap.rate.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import ru.pap.rate.R;
import ru.pap.rate.RateApplication;

/**
 * Created by alex on 14.11.16.
 */

public class RateSyncAdapterHelper {


    private static final long SYNC_INTERVAL = 4 * 60L;
    private static Account sAccount;


    public static boolean requestManual(Context context, SyncType syncType) {
        Account acc = getAccount(context);
        if (acc != null) {
            enableSyncInterval(acc);
            return requestManualSync(acc, syncType);
        }
        return false;
    }

    private static boolean requestManualSync(Account account, SyncType syncType) {
        if (account == null) {
            return false;
        }
        if (ContentResolver.isSyncActive(account, getAuthority())) {
            ContentResolver.cancelSync(account, getAuthority());
        }
        Bundle bundle = new Bundle();
        bundle.putString(RateSyncAdapter.SYNC_TYPE_PARAM, syncType.name());
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(account, getAuthority(), bundle);
        ContentResolver.setIsSyncable(account, getAuthority(), 1);
        return true;
    }

    private static void enableSyncInterval(Account acc) {
        Bundle bundle = new Bundle();
        bundle.putString(RateSyncAdapter.SYNC_TYPE_PARAM, SyncType.ALL.name());
        ContentResolver.addPeriodicSync(
                acc,
                getAuthority(),
                bundle,
                SYNC_INTERVAL);
    }

    public static boolean isSyncable(Context context) {
        return ContentResolver.getIsSyncable(getAccount(context), getAuthority()) > 0;
    }

    public static void onValidSync(Context context) {
        requestManual(context, SyncType.NONE);
    }

    private static String getAuthority() {
        return RateApplication.getAppContext().getString(R.string.content_authority);
    }

    private static String getAccountType(Context context) {
        return context.getString(R.string.account_type);
    }

    public static Account getAccount(Context context) {
        return onCreateAccount(context);
    }

    private static Account onCreateAccount(Context context) {
        final AccountManager am = AccountManager.get(context);
        if (sAccount == null) {
            sAccount = new Account(context.getString(R.string.app_name), getAccountType(context));
        }
        if (am.addAccountExplicitly(sAccount, context.getPackageName(), new Bundle())) {
            ContentResolver.setSyncAutomatically(sAccount, getAuthority(), true);
        }
        return sAccount;
    }


}
