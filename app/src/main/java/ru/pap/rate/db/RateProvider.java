package ru.pap.rate.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by alex on 10.11.16.
 */

public class RateProvider extends ContentProvider {


    private RateDatabase mRateDatabase;
    private UriMatcher mUriMatcher;

    @Override
    public boolean onCreate() {
        mRateDatabase = new RateDatabase(getContext());
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(RateDatabase.CONTENT_AUTHORITY, UriPathEnum.QUOTE.path, UriPathEnum.QUOTE.code);
        mUriMatcher.addURI(RateDatabase.CONTENT_AUTHORITY, UriPathEnum.QUOTE_ID.path, UriPathEnum.QUOTE_ID.code);
        mUriMatcher.addURI(RateDatabase.CONTENT_AUTHORITY, UriPathEnum.SYMBOL.path, UriPathEnum.SYMBOL.code);
        mUriMatcher.addURI(RateDatabase.CONTENT_AUTHORITY, UriPathEnum.SYMBOL_ID.path, UriPathEnum.SYMBOL_ID.code);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mRateDatabase.getReadableDatabase();
        UriPathEnum pathEnum = findPathEnum(uri);
        Cursor cursor = null;
        switch (pathEnum) {
            case QUOTE:
                cursor = database.query(QuoteContract.QUOTE_JOIN_SYMBOL, projection, selection, selectionArgs,
                        null, null, sortOrder, getLimitOffset(uri));
                break;
            case QUOTE_ID:
                cursor = database.query(pathEnum.table, projection,
                        onMergeSelection(selection, QuoteContract.Quote._ID + " =? "),
                        onMergeSelectionArgs(selectionArgs, new String[]{uri.getLastPathSegment()}),
                        null, null, sortOrder);
                break;
            case SYMBOL:
                cursor = database.query(pathEnum.table, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SYMBOL_ID:
                cursor = database.query(pathEnum.table, projection,
                        onMergeSelection(selection, SymbolContract.Symbol.SYMBOL + " =? "),
                        onMergeSelectionArgs(selectionArgs, new String[]{uri.getLastPathSegment()}),
                        null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("No such table to query");
        }
        Context context = getContext();
        if (context!=null && cursor!=null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        UriPathEnum pathEnum = findPathEnum(uri);
        return pathEnum.contentType;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        UriPathEnum pathEnum = findPathEnum(uri);
        if (pathEnum == null) {
            throw new UnsupportedOperationException("No such table to query");
        }
        SQLiteDatabase database = mRateDatabase.getWritableDatabase();
        long rowId = database.insertWithOnConflict(pathEnum.table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Uri resultUri;
        switch (pathEnum) {
            case QUOTE:
                resultUri = QuoteContract.buildQueryIDUri(rowId);
                break;
            case SYMBOL:
                String symbol = contentValues.getAsString(SymbolContract.Symbol.SYMBOL);
                resultUri = SymbolContract.buildQuerySymbolUri(symbol);
                break;
            default:
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
        }
        return resultUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase database = mRateDatabase.getWritableDatabase();
        UriPathEnum pathEnum = findPathEnum(uri);
        if (pathEnum == null) {
            throw new UnsupportedOperationException("No such table to query");
        }
        else {
            int numInserted = 0;
            database.beginTransaction();
            try {
                for (ContentValues contentValues : values) {
                    long id = database.insertWithOnConflict(pathEnum.table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                    if (id > 0) {
                        numInserted++;
                    }
                }
                database.setTransactionSuccessful();
            }
            finally {
                database.endTransaction();
            }
            return numInserted;
        }
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        UriPathEnum pathEnum = findPathEnum(uri);
        SQLiteDatabase database = mRateDatabase.getWritableDatabase();
        return database.delete(pathEnum.table, whereClause, whereArgs);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    private UriPathEnum findPathEnum(Uri uri) {
        int code = mUriMatcher.match(uri);
        UriPathEnum pathEnum = UriPathEnum.findPathEnum(code);
        if (pathEnum == null) {
            throw new UnsupportedOperationException("Unknown uri with code " + code);
        }
        return pathEnum;
    }

    private String getLimitOffset(Uri uri) {
        String limit = uri.getQueryParameter(QuoteContract.LIMIT);
        String offset = uri.getQueryParameter(QuoteContract.OFFSET);
        StringBuilder builder = new StringBuilder();
        if (TextUtils.isEmpty(offset)) {
            builder.append(",").append(offset);
        }
        if (TextUtils.isEmpty(limit)) {
            return null;
        }
        builder.append(limit);
        return builder.toString();
    }

    private String onMergeSelection(String selection, String addSelection) {
        if (TextUtils.isEmpty(selection)) {
            return addSelection;
        }
        return "(" +
                selection + ") and (" +
                addSelection + ")";

    }

    static String[] onMergeSelectionArgs(String[]... arrays) {
        int length = 0;
        for (String[] array : arrays) {
            if(array == null){
                continue;
            }
            length += array.length;
        }

        final String[] result = new String[length];

        int offset = 0;
        for (String[] array : arrays) {
            if(array == null){
                continue;
            }
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }
}
