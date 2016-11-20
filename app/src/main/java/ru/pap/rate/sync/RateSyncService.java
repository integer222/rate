package ru.pap.rate.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by alex on 14.11.16.
 */

public class RateSyncService extends Service {

    private static final Object sMonitor = new Object();
    private static RateSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sMonitor) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new RateSyncAdapter(getApplicationContext(), false);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
