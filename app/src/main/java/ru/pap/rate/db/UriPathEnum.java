package ru.pap.rate.db;

import android.content.UriMatcher;

/**
 * Created by alex on 10.11.16.
 */

public enum UriPathEnum {

    QUOTE(1, "quote", "quote", false, QuoteContract.TABLE),
    QUOTE_ID(2, "quote/*", "quote_id", true, QuoteContract.TABLE),

    SYMBOL(4, "symbol", "symbol", false, SymbolContract.TABLE),
    SYMBOL_ID(5, "symbol/*", "symbol_id", true, SymbolContract.TABLE);


    public int code;
    public String path;
    public String contentType;
    public String table;

    UriPathEnum(int code, String path, String contentType, boolean item, String table) {
        this.code = code;
        this.path = path;
        this.contentType = (item ? RateDatabase.CONTENT_ITEM_TYPE_BASE : RateDatabase.CONTENT_TYPE_BASE) + contentType;
        this.table = table;
    }

    public static UriPathEnum findPathEnum(int code) {
        for (UriPathEnum pathEnum : values()) {
            if (pathEnum.code == code) {
                return pathEnum;
            }
        }
        return null;
    }

    public static UriPathEnum findPathEnum(String tableName) {
        for (UriPathEnum pathEnum : values()) {
            if (pathEnum.table.equals(tableName)) {
                return pathEnum;
            }
        }
        return null;
    }
}
