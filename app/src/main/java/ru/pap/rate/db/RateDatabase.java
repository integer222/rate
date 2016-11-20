package ru.pap.rate.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by alex on 10.11.16.
 */

public class RateDatabase extends SQLiteOpenHelper {

    public static final String CONTENT_AUTHORITY = "ru.pap.rate";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String CONTENT_TYPE_BASE = "vnd.android.cursor.dir/vnd.selfish.";

    public static final String CONTENT_ITEM_TYPE_BASE = "vnd.android.cursor.item/vnd.selfish.";


    private static final String DATABASE_NAME = "rate.db";
    private static final int VERSION_DB = 5;




    public RateDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SymbolContract.Symbol.CREATE);
        db.execSQL(QuoteContract.Quote.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SymbolContract.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + QuoteContract.TABLE);
        onCreate(db);
    }
}
