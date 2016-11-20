package ru.pap.rate.service;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.ReadablePartial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.pap.rate.Config;
import ru.pap.rate.api.CoreApi;
import ru.pap.rate.api.queries.SymbolValuesQuery;
import ru.pap.rate.db.DataMapper;
import ru.pap.rate.db.QuoteContract;
import ru.pap.rate.db.RateDatabase;
import ru.pap.rate.db.SymbolContract;
import ru.pap.rate.db.UriPathEnum;
import ru.pap.rate.model.Quote;
import ru.pap.rate.model.QuotesContainer;
import ru.pap.rate.model.Symbol;
import ru.pap.rate.model.SymbolsContainer;
import ru.pap.rate.service.QuoteSyncParam.StateSync;
import ru.pap.rate.widget.SymbolQuoteWidget;

/**
 * Created by alex on 11.11.16.
 */

public class LoadDataHelper {

    public static final String KEY_ACTION = "action";
    public static final String ACTION_COMPLITE = "action_complite";
    public static final String ACTION_ERROR = "action_error";

    public static final String QUERY_PARAMS = "query_params";
    private Context mContext;
    private DataMapper mDataMapper = new DataMapper();

    public LoadDataHelper(Context context) {
        mContext = context;
    }

    public boolean onSync(Bundle bundle) {
        BaseSyncParam baseSyncParam = (BaseSyncParam) bundle.getSerializable(QUERY_PARAMS);
        if (baseSyncParam == null) {
            return false;
        }
        return onSync(baseSyncParam);
    }

    public boolean onSync(BaseSyncParam baseSyncParam) {

        UriPathEnum uriPathEnum = UriPathEnum.findPathEnum(baseSyncParam.getTableName());
        if (uriPathEnum == null) {
            return false;
        }
        try {
            switch (uriPathEnum) {
                case QUOTE:
                    onSyncQuote((QuoteSyncParam) baseSyncParam);
                    break;
                case SYMBOL:
                    onSyncSymbol((SymbolSyncParam) baseSyncParam);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            mContext.getContentResolver().notifyChange(RateDatabase.BASE_CONTENT_URI.buildUpon()
                    .appendPath(uriPathEnum.path).appendQueryParameter(KEY_ACTION, ACTION_ERROR).build(), null, false);
            mContext.sendBroadcast(SymbolQuoteWidget.getIntentUpdate(mContext));
            return false;
        }
        mContext.getContentResolver().notifyChange(RateDatabase.BASE_CONTENT_URI.buildUpon()
                .appendPath(uriPathEnum.path).appendQueryParameter(KEY_ACTION, ACTION_COMPLITE).build(), null, false);
        mContext.sendBroadcast(SymbolQuoteWidget.getIntentUpdate(mContext));
        return false;
    }

    private boolean onSyncQuote(QuoteSyncParam param) throws IOException {
        if (TextUtils.isEmpty(param.getSymbol())) {
            return false;
        }
        Date[] betweenDate;
        Cursor cursor;
        Date dateTs;
        boolean flagRemoveAll = false;
        int limit;
        switch (param.getStateSync()) {
            case UP:
            case DOWN:
                boolean direction = param.getStateSync().equals(StateSync.UP);
                cursor = QuoteContract.onQueryQuoteMaxOrMin(mContext, param.getSymbol(), direction);
                if (!cursor.moveToFirst()) {
                    return false;
                }
                dateTs = mDataMapper.getDateMapper(cursor, QuoteContract.Quote.DATE);
                dateTs = addDays(dateTs, direction ? 1 : -1);
                limit = direction ? param.getLimit() : -param.getLimit();
                break;
            case SYNC:
            default:
                cursor = QuoteContract.onQueryQuoteMaxOrMin(mContext, param.getSymbol(), true);
                if (!cursor.moveToFirst()) {
                    dateTs = new Date();
                } else {
                    dateTs = mDataMapper.getDateMapper(cursor, QuoteContract.Quote.DATE);
                    dateTs = addDays(dateTs, 1);
                }
                int countDate = Math.abs(Days.daysBetween(DateTime.now(), new DateTime(dateTs)).getDays());
                if (countDate > Config.MAX_SYNC_ELEMENTS || countDate == 0) {
                    flagRemoveAll = true;
                    dateTs = new Date();
                    limit = -param.getLimit();
                } else {
                    limit = countDate;
                }
                break;
        }
        cursor.close();


        betweenDate = getBetweenDate(dateTs, limit);

        SymbolValuesQuery symbolValuesQuery =
                SymbolValuesQuery.newInstance(param.getSymbol(), betweenDate[0], betweenDate[1]);
        Call<QuotesContainer> call = CoreApi.getInstance().getQuoteApi().getSymbolValues(symbolValuesQuery);
        Response<QuotesContainer> response = call.execute();
        QuotesContainer container = response.body();
        if (flagRemoveAll) {
            QuoteContract.onDeleteSymbolQuotes(mContext, param.getSymbol());
        }
        mContext.getContentResolver().bulkInsert(QuoteContract.buildBaseUri(), onBuildQuote(container));
        Uri uri = QuoteContract.buildBaseUri();
        mContext.getContentResolver().notifyChange(uri, null, false);
        return true;
    }

    private Date[] getBetweenDate(Date oneDate, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oneDate);
        calendar.add(Calendar.DATE, offset);
        Date twoDate = calendar.getTime();
        return oneDate.before(twoDate) ? new Date[]{oneDate, twoDate} : new Date[]{twoDate, oneDate};
    }

    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    private boolean onSyncSymbol(SymbolSyncParam param) throws IOException {

        if (!param.isRefresh()) {
            if (!isEmptyCheck()) {
                return false;
            }
        }
        Call<SymbolsContainer> call = CoreApi.getInstance().getSymbolsApi().getSymbols();
        Response<SymbolsContainer> response = call.execute();
        SymbolsContainer container = response.body();
        mContext.getContentResolver().delete(SymbolContract.buildBaseUri(), null, null);
        mContext.getContentResolver().bulkInsert(SymbolContract.buildBaseUri(), onBuildSymbol(container));
        Uri uri = SymbolContract.buildBaseUri();
        mContext.getContentResolver().notifyChange(uri, null, false);
        return true;
    }

    private boolean isEmptyCheck() {
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(QuoteContract.buildBaseUri(1, 0), null, null, null, null);
            return cursor != null && cursor.getCount() == 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private ContentValues[] onBuildSymbol(SymbolsContainer symbolsContainer) {
        if (symbolsContainer == null || symbolsContainer.getSymbols().isEmpty()) {
            return new ContentValues[0];
        }
        List<ContentValues> contentValuesList = new ArrayList<>(symbolsContainer.getSymbols().size());
        ContentValues contentValues;
        for (Symbol symbol : symbolsContainer.getSymbols()) {
            contentValues = new ContentValues();
            contentValues.put(SymbolContract.Symbol.SYMBOL, symbol.getSymbol());
            contentValues.put(SymbolContract.Symbol.NAME, symbol.getName());
            contentValues.put(SymbolContract.Symbol.TYPE, symbol.getName());
            contentValues.put(SymbolContract.Symbol.PRICE, symbol.getPrice());
            contentValues.put(SymbolContract.Symbol.TS, symbol.getTs());
            contentValues.put(SymbolContract.Symbol.UTCTIME, symbol.getUtctime().getTime());
            contentValues.put(SymbolContract.Symbol.VOLUME, symbol.getVolume());
            contentValuesList.add(contentValues);
        }
        return contentValuesList.toArray(new ContentValues[0]);
    }

    private ContentValues[] onBuildQuote(QuotesContainer quotesContainer) {
        if (quotesContainer == null || quotesContainer.getQuotes().isEmpty()) {
            return new ContentValues[0];
        }
        List<ContentValues> contentValuesList = new ArrayList<>(quotesContainer.getQuotes().size());
        ContentValues contentValues;
        for (Quote quote : quotesContainer.getQuotes()) {
            contentValues = new ContentValues();
            contentValues.put(QuoteContract.Quote.SYMBOL, quote.getSymbol());
            contentValues.put(QuoteContract.Quote.DATE, quote.getDate().getTime());
            contentValues.put(QuoteContract.Quote.OPEN, quote.getOpen());
            contentValues.put(QuoteContract.Quote.HIGH, quote.getHigh());
            contentValues.put(QuoteContract.Quote.LOW, quote.getLow());
            contentValues.put(QuoteContract.Quote.CLOSE, quote.getClose());
            contentValues.put(QuoteContract.Quote.VOLUME, quote.getVolume());
            contentValues.put(QuoteContract.Quote.ADJCLOSE, quote.getAdjClose());
            contentValuesList.add(contentValues);
        }
        return contentValuesList.toArray(new ContentValues[0]);
    }


}
