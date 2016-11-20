package ru.pap.rate.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.pap.rate.R;
import ru.pap.rate.db.DataMapper;
import ru.pap.rate.holders.QuoteHolder;
import ru.pap.rate.model.Quote;

/**
 * Created by alex on 12.11.16.
 */

public class QuoteAdapter extends BaseFooterAdapter<QuoteHolder> {

    private DataMapper mDataMapper = new DataMapper();

    public QuoteAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public void onBindViewHolder(QuoteHolder viewHolder, Cursor cursor) {
        viewHolder.fill(mDataMapper.fromCursorJoinQuoteAndSymbol(cursor));
    }

    @Override
    public QuoteHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_quote_card, parent, false);
        return new QuoteHolder(view);
    }

    @Override
    protected Quote getItemModel(Cursor cursor) {
        return mDataMapper.fromCursorJoinQuoteAndSymbol(cursor);
    }
}
