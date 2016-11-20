package ru.pap.rate.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by alex on 11.11.16.
 */

public class LoadDataService extends IntentService {

    public static final String DATA_PARAM = "data_param";


    public LoadDataService() {
        super("LoadDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent == null ){
            return;
        }
        Bundle data =  intent.getBundleExtra(DATA_PARAM);
        if(data == null){
            return;
        }
        new LoadDataHelper(getApplicationContext()).onSync(data);


    }
}
