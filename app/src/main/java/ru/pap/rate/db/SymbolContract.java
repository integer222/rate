package ru.pap.rate.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alex on 10.11.16.
 */

public class SymbolContract {

    public static final String TABLE = "symbol";

    private SymbolContract(){

    }

    private interface SymbolColumns {
        String NAME = "name";
        String SYMBOL = "symbol_symbol";
        String PRICE = "price";
        String TS = "ts";
        String TYPE = "type";
        String UTCTIME = "utctime";
        String VOLUME = "volume";
    }

    public static class Symbol implements BaseColumns, SymbolColumns{
        public static final String CREATE = "CREATE TABLE " + TABLE + " ( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT, "
                + SYMBOL + " TEXT NOT NULL, "
                + PRICE + " REAL, "
                + TS + " INTEGER, "
                + TYPE + " TEXT, "
                + UTCTIME + " INTEGER, "
                + VOLUME + " INTEGER, "
                + "UNIQUE (" + SYMBOL + ") ON CONFLICT REPLACE)";
    }

    public static Uri buildQuerySymbolUri(String symbol){
        return RateDatabase.BASE_CONTENT_URI.buildUpon().appendPath(TABLE).appendPath(symbol).build();
    }

    public static Uri buildBaseUri(){
        return RateDatabase.BASE_CONTENT_URI.buildUpon().appendPath(TABLE).build();
    }

}
