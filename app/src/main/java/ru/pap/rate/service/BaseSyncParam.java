package ru.pap.rate.service;

import java.io.Serializable;

/**
 * Created by alex on 11.11.16.
 */

public class BaseSyncParam implements Serializable{

    private String mTableName;


    public BaseSyncParam(String tableName) {
        mTableName = tableName;
    }

    public String getTableName() {
        return mTableName;
    }


}
