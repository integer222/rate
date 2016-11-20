package ru.pap.rate.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.pap.rate.adapters.IHolderClick;

/**
 * Created by alex on 13.11.16.
 */

public abstract class ClickViewHolder extends RecyclerView.ViewHolder {

    public ClickViewHolder(View itemView, final IHolderClick holderClick) {
        super(itemView);
        if (holderClick != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holderClick.onItemClick(getAdapterPosition(), view);
                }
            });
        }
    }
}
