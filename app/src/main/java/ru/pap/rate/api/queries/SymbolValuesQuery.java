package ru.pap.rate.api.queries;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alex on 10.11.16.
 */

public class SymbolValuesQuery {

    private static final String SELECT_QUERY_TEMPLATE = "select * from yahoo.finance.historicaldata  " +
            "where symbol = \"%1$s\" and startDate = \"%2$s\" and endDate = \"%3$s\"";

    private String mSymbol;
    private Date mDateStart;
    private Date mDateEnd;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static SymbolValuesQuery newInstance(String symbol, Date dateStart, Date dateEnd) {
        SymbolValuesQuery symbolValuesQuery = new SymbolValuesQuery();
        symbolValuesQuery.mSymbol = symbol;
        symbolValuesQuery.mDateStart = dateStart;
        symbolValuesQuery.mDateEnd = dateEnd;
        return symbolValuesQuery;
    }

    private SymbolValuesQuery() {
    }

    public String getSymbol() {
        return mSymbol;
    }

    public Date getDateStart() {
        return mDateStart;
    }

    public Date getDateEnd() {
        return mDateEnd;
    }

    @Override
    public String toString() {
        return String.format(SELECT_QUERY_TEMPLATE, getSymbol(),
                mDateFormat.format(getDateStart()), mDateFormat.format(getDateEnd()));
    }
    //https://query.yahooapis.com/v1/public/yql?q=
    // select * from yahoo.finance.historicaldata  where symbol = "RUB=X" and startDate = "2009-09-11" and endDate = "2010-03-10"
    // &format=json
    // &env=store://datatables.org/alltableswithkeys&callback=
}
