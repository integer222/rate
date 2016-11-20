package ru.pap.rate.db;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alex on 10.11.16.
 */

public class QuoteContract extends BaseContract {

    public static final String TABLE = "quote";
    public static String QUOTE_JOIN_SYMBOL = SymbolContract.TABLE + " as s "
            + " JOIN " + TABLE + " as q ON "
            + "s." + SymbolContract.Symbol.SYMBOL + "=q." + QuoteColumns.SYMBOL + " ";


    private QuoteContract() {

    }


    private interface QuoteColumns {
        String SYMBOL = "quote_symbol";
        String DATE = "date";
        String OPEN = "open";
        String HIGH = "high";
        String LOW = "low";
        String CLOSE = "close";
        String VOLUME = "volume";
        String ADJCLOSE = "adjClose";
    }

    public static class Quote implements BaseColumns, QuoteColumns {
        public static final String CREATE = "CREATE TABLE " + TABLE + " ( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SYMBOL + " TEXT NOT NULL, "
                + DATE + " INTEGER, "
                + OPEN + " REAL, "
                + HIGH + " REAL, "
                + LOW + " REAL, "
                + CLOSE + " REAL, "
                + VOLUME + " TEXT, "
                + ADJCLOSE + " REAL) ";
       // + "UNIQUE (" + SYMBOL+","+ DATE + ") ON CONFLICT REPLACE)";


    }


    public static Cursor onQueryFeed(Context context, String symbol, int limit, int offset) {
        String[] colomns = {
                SymbolContract.Symbol.NAME,
                QuoteColumns.SYMBOL,
                QuoteColumns.DATE,
                QuoteColumns.OPEN,
                QuoteColumns.HIGH,
                QuoteColumns.LOW,
                QuoteColumns.CLOSE
        };
        Uri uri = buildBaseUri(limit, offset);
        return context.getContentResolver().query(uri, colomns, "s.symbol_symbol = ?", new String[]{symbol}, "q.date DESC");
    }

    public static Cursor onQueryId(Context context, long id) {
        Uri uri = RateDatabase.BASE_CONTENT_URI.buildUpon()
                .appendPath(UriPathEnum.QUOTE.path)
                .appendPath(String.valueOf(id))
                .build();
        return context.getContentResolver().query(uri, null, null, null, null);
    }

    public static Cursor onQueryQuoteMaxOrMin(Context context, String symbol, boolean max) {
        Uri uri = buildBaseUri(1, 0);
        return context.getContentResolver().query(uri, null, "s.symbol_symbol = ?",
                new String[]{symbol}, "q.date " + (max ? "DESC" : "ASC"));
    }

    public static int onDeleteSymbolQuotes(Context context, String symbol) {
        return context.getContentResolver().delete(buildBaseUri(), Quote.SYMBOL +" = ?", new String[]{symbol});
    }

    public static Uri buildBaseUri(int limit, int offset) {
        return RateDatabase.BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE)
                .appendQueryParameter(LIMIT, String.valueOf(limit))
                .appendQueryParameter(OFFSET, String.valueOf(offset))
                .build();
    }

    public static Uri buildQueryIDUri(long id) {
        return RateDatabase.BASE_CONTENT_URI.buildUpon().appendPath(TABLE).appendPath(String.valueOf(id)).build();
    }

    public static Uri buildBaseUri() {
        return RateDatabase.BASE_CONTENT_URI.buildUpon().appendPath(TABLE).build();
    }


}
