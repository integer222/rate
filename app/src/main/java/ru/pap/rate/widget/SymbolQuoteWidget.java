package ru.pap.rate.widget;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ru.pap.rate.R;
import ru.pap.rate.ShareUtils;
import ru.pap.rate.model.Quote;
import ru.pap.rate.service.QuoteSyncParam;
import ru.pap.rate.service.LoadDataHelper;
import ru.pap.rate.service.LoadDataService;


public class SymbolQuoteWidget extends AppWidgetProvider {

    public static final String QUOTE_RESULT = "quote_result";
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

    private Quote mQuote;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = onViewsUpdate(context);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews onViewsUpdate(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.symbol_quote_widget);

        Quote fillQuote = mQuote;
        if (fillQuote == null) {
            fillQuote = new Quote();
        }

        views.setTextViewText(R.id.symbol_name, fillQuote.getName());
        StringBuilder builder = new StringBuilder(context.getString(R.string.quotation));
        builder.append(": ")
                .append(fillQuote.getOpen()).append("/")
                .append(fillQuote.getHigh()).append("/")
                .append(fillQuote.getLow()).append("/")
                .append(fillQuote.getClose());
        views.setTextViewText(R.id.quote_text, builder.toString());

        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
        views.setTextViewText(R.id.date_view, fillQuote.getDate() != null ? dateFormat.format(fillQuote.getDate()) : "-");

        Intent shareIntent = ShareUtils.onCreateShareTextIntent(
                fillQuote.getName() + ": " + fillQuote.getClose() + " " +
                        (fillQuote.getDate() != null ? dateFormat.format(fillQuote.getDate()) : "-"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, shareIntent, 0);
        views.setOnClickPendingIntent(R.id.share_btn, pendingIntent);

        if(mQuote != null){
            pendingIntent = PendingIntent.getService(context, 0, getIntentSyncService(context), 0);
            views.setOnClickPendingIntent(R.id.refresh, pendingIntent);
        }
        views.setViewVisibility(R.id.refresh, isCheckData() ? View.GONE : View.VISIBLE);
        views.setTextColor(R.id.quote_text, context.getResources()
                .getColor((isCheckData() ? R.color.main_text_color : R.color.error_text_color)));
        return views;
    }


    private boolean isCheckData() {
        if (mQuote == null || mQuote.getDate() == null) {
            return true;
        }

        int countDay = Days.daysBetween(DateTime.now(), new DateTime(mQuote.getDate())).getDays();
        countDay = Math.abs(countDay);
        if (countDay > 1) {
            return false;
        }
        return true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_WIDGET_RECEIVER.equals(action)) {
            mQuote = (Quote) intent.getSerializableExtra(QUOTE_RESULT);
            RemoteViews remoteViews = onViewsUpdate(context);
            ComponentName thisWidget = new ComponentName(context, SymbolQuoteWidget.class);
            AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteViews);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

    }

    @Override
    public void onEnabled(Context context) {
        context.sendBroadcast(getIntentUpdate(context));
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static Intent getIntentUpdate(Context context) {
        Intent intent = new Intent(context, BroadcastWidgetUpdate.class);
        intent.setAction(BroadcastWidgetUpdate.ACTION_UPDATE_WIDGET_RECEIVER);
        return intent;
    }

    private Intent getIntentSyncService(Context context){
        QuoteSyncParam quoteSyncParam = new QuoteSyncParam(mQuote.getSymbol());
        quoteSyncParam.setStateSync(QuoteSyncParam.StateSync.SYNC);
        Bundle bundle = new Bundle();
        bundle.putSerializable(LoadDataHelper.QUERY_PARAMS, quoteSyncParam);
        Intent intent = new Intent(context, LoadDataService.class);
        intent.putExtra(LoadDataService.DATA_PARAM, bundle);
        return intent;
    }
}

