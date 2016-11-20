package ru.pap.rate.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by alex on 13.11.16.
 */

public abstract class BaseClickAdapter<T extends RecyclerView.ViewHolder> extends BaseFooterAdapter<T> implements IHolderClick{

    private IHolderClick mHolderClick;

    public BaseClickAdapter(Cursor cursor, IHolderClick holderClick) {
        super(cursor);
        mHolderClick = holderClick;
    }

    @Override
    public void onItemClick(int position, View view) {
        if(mHolderClick == null){
            return;
        }
        mHolderClick.onItemClick(position, view);
    }
}
