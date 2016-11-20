package ru.pap.rate.db;

import android.database.Cursor;

import java.util.Date;

import ru.pap.rate.model.Quote;
import ru.pap.rate.model.Symbol;

/**
 * Created by alex on 12.11.16.
 */

public class DataMapper {


    public Quote fromCursorJoinQuoteAndSymbol(Cursor cursor) {
        Quote quote = new Quote();
        quote.setName(cursor.getString(cursor.getColumnIndex(SymbolContract.Symbol.NAME)));
        quote.setSymbol(cursor.getString(cursor.getColumnIndex(QuoteContract.Quote.SYMBOL)));
        quote.setDate(new Date(cursor.getLong(cursor.getColumnIndex(QuoteContract.Quote.DATE))));
        quote.setAdjClose(cursor.getDouble(cursor.getColumnIndex(QuoteContract.Quote.ADJCLOSE)));
        quote.setOpen(cursor.getDouble(cursor.getColumnIndex(QuoteContract.Quote.OPEN)));
        quote.setHigh(cursor.getDouble(cursor.getColumnIndex(QuoteContract.Quote.HIGH)));
        quote.setLow(cursor.getDouble(cursor.getColumnIndex(QuoteContract.Quote.LOW)));
        quote.setClose(cursor.getDouble(cursor.getColumnIndex(QuoteContract.Quote.CLOSE)));
        quote.setVolume(cursor.getString(cursor.getColumnIndex(QuoteContract.Quote.VOLUME)));
        return quote;
    }

    public Symbol fromCursorSymbol(Cursor cursor) {
        Symbol symbol = new Symbol();
        symbol.setName(cursor.getString(cursor.getColumnIndex(SymbolContract.Symbol.NAME)));
        symbol.setSymbol(cursor.getString(cursor.getColumnIndex(SymbolContract.Symbol.SYMBOL)));
        symbol.setPrice(cursor.getDouble(cursor.getColumnIndex(SymbolContract.Symbol.PRICE)));
        symbol.setTs(cursor.getLong(cursor.getColumnIndex(SymbolContract.Symbol.TS)));
        symbol.setType(cursor.getString(cursor.getColumnIndex(SymbolContract.Symbol.TYPE)));
        symbol.setUtctime(getDateMapper(cursor, SymbolContract.Symbol.UTCTIME));
        symbol.setVolume(cursor.getLong(cursor.getColumnIndex(SymbolContract.Symbol.VOLUME)));
        return symbol;
    }

    public Date getDateMapper(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index < 0) {
            return null;
        }
        return new Date(cursor.getLong(index));
    }


}
