package ru.pap.rate.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import ru.pap.rate.R;

/**
 * Created by alex on 12.11.16.
 */

public class QuoteViewCard extends ConstraintLayout {

    private TextView mSymbolName;
    private TextView mQuoteText;
    private TextView mDateText;
    private ImageView mShare;

    public QuoteViewCard(Context context) {
        super(context);
    }

    public QuoteViewCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuoteViewCard(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mSymbolName = (TextView) findViewById(R.id.symbol_name);
        mQuoteText = (TextView)findViewById(R.id.quote_text);
        mShare = (ImageView) findViewById(R.id.share_btn);
        mDateText = (TextView) findViewById(R.id.date_view);
    }

    public TextView getSymbolName() {
        return mSymbolName;
    }

    public TextView getQuoteText() {
        return mQuoteText;
    }

    public ImageView getShare() {
        return mShare;
    }

    public TextView getDateText() {
        return mDateText;
    }

}
