package ru.pap.rate.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;

import ru.pap.rate.UserPreferences;
import ru.pap.rate.db.DataMapper;
import ru.pap.rate.db.QuoteContract;
import ru.pap.rate.model.Quote;

/**
 * Created by alex on 13.11.16.
 */

public class BroadcastWidgetUpdate extends BroadcastReceiver {

    public static String ACTION_UPDATE_WIDGET_RECEIVER = "ActionUpdateWidget";

    @Override
    public void onReceive(Context context, Intent intent) {
        Cursor cursor = null;
        try {
            String mCurrentSymbol = UserPreferences.getPreferences().getCurrentSymbol();
            Quote quote = null;
            if (!TextUtils.isEmpty(mCurrentSymbol)) {
                cursor = QuoteContract.onQueryQuoteMaxOrMin(context, mCurrentSymbol, true);
                if (cursor.moveToFirst()) {
                    quote = new DataMapper().fromCursorJoinQuoteAndSymbol(cursor);
                }
            }
            Intent resultIntent = new Intent(context, SymbolQuoteWidget.class);
            resultIntent.putExtra(SymbolQuoteWidget.QUOTE_RESULT, quote);
            resultIntent.setAction(SymbolQuoteWidget.ACTION_WIDGET_RECEIVER);
            context.sendBroadcast(resultIntent);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }
}
