package ru.pap.rate.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.pap.rate.R;
import ru.pap.rate.db.DataMapper;
import ru.pap.rate.holders.SymbolHolder;
import ru.pap.rate.model.Symbol;

/**
 * Created by alex on 12.11.16.
 */

public class SymbolAdapter extends BaseClickAdapter<SymbolHolder>{

    DataMapper mDataMapper = new DataMapper();

    public SymbolAdapter(Cursor cursor, IHolderClick holderClick) {
        super(cursor, holderClick);
    }

    @Override
    public SymbolHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_symbol_card, parent, false);
        return new SymbolHolder(view, this);
    }

    @Override
    public void onBindViewHolder(SymbolHolder viewHolder, Cursor cursor) {
        viewHolder.fill(mDataMapper.fromCursorSymbol(cursor));
    }

    @Override
    protected Symbol getItemModel(Cursor cursor) {
        return mDataMapper.fromCursorSymbol(cursor);
    }


}
