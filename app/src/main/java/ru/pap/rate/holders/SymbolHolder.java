package ru.pap.rate.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.pap.rate.adapters.IHolderClick;
import ru.pap.rate.model.Symbol;
import ru.pap.rate.view.SymbolViewCard;

/**
 * Created by alex on 12.11.16.
 */

public class SymbolHolder extends ClickViewHolder {


    public SymbolHolder(View itemView, IHolderClick holderClick) {
        super(itemView, holderClick);
    }

    public void fill(Symbol symbol) {
        SymbolViewCard symbolViewCard = (SymbolViewCard) itemView;
        symbolViewCard.getSymbolName().setText(symbol.getName());
    }
}
