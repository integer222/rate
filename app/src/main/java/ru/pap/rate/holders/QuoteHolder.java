package ru.pap.rate.holders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ru.pap.rate.R;
import ru.pap.rate.ShareUtils;
import ru.pap.rate.model.Quote;
import ru.pap.rate.view.QuoteViewCard;

/**
 * Created by alex on 09.11.16.
 */

public class QuoteHolder extends RecyclerView.ViewHolder {
    public QuoteHolder(View itemView) {
        super(itemView);
    }

    public void fill(final Quote quote) {
        QuoteViewCard quoteViewCard = (QuoteViewCard) itemView;
        quoteViewCard.getSymbolName().setText(quote.getName());
        StringBuilder builder = new StringBuilder(itemView.getContext().getResources().getString(R.string.quotation));
        builder.append(": ")
                .append(quote.getOpen()).append("/")
                .append(quote.getHigh()).append("/")
                .append(quote.getLow()).append("/")
                .append(quote.getClose());
        quoteViewCard.getQuoteText().setText(builder.toString());

        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
        quoteViewCard.getDateText().setText(dateFormat.format(quote.getDate()));

        quoteViewCard.getShare().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ShareUtils.onCreateShareTextIntent(
                        quote.getName() + ": " + quote.getClose() + " " +
                                SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(quote.getDate()));
                view.getContext().startActivity(intent);
            }
        });
    }
}
