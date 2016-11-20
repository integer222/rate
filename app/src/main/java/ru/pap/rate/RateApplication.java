package ru.pap.rate;

import com.facebook.stetho.Stetho;

import android.app.Application;
import android.content.Context;

import ru.pap.rate.sync.RateSyncAdapterHelper;
import ru.pap.rate.sync.SyncType;

/**
 * Created by alex on 09.11.16.
 */

public class RateApplication extends Application {

    private static RateApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (BuildConfig.DEBUG) {
            stethoInit();
        }
        RateSyncAdapterHelper.requestManual(this, SyncType.ALL);

    }

    public static RateApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }

    private void stethoInit() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

    }
}
