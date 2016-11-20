package ru.pap.rate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.pap.rate.R;

/**
 * Created by alex on 12.11.16.
 */

public class SymbolViewCard extends LinearLayout {

    private TextView mSymbolName;

    public SymbolViewCard(Context context) {
        super(context);
    }

    public SymbolViewCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SymbolViewCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(isInEditMode()){
            return;
        }
        onInit();
    }

    private void onInit() {
        mSymbolName = (TextView)findViewById(R.id.symbol_name);
    }

    public TextView getSymbolName() {
        return mSymbolName;
    }
}
