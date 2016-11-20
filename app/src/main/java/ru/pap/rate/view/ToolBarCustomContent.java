package ru.pap.rate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import ru.pap.rate.R;

/**
 * Created by alex on 13.11.16.
 */

public class ToolBarCustomContent extends FrameLayout {

    private View mTitleContent;
    private TextView mTitle;
    private EditText mSearchText;



    public ToolBarCustomContent(Context context) {
        super(context);
    }

    public ToolBarCustomContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolBarCustomContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            return;
        }
        onInit();
    }

    private void onInit() {
        mTitleContent = findViewById(R.id.title_content);
        mTitleContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                expand();
            }
        });
        mTitle = (TextView) findViewById(R.id.symbol_select_name);
        mSearchText = (EditText) findViewById(R.id.search_symbol);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void expand() {
        mTitleContent.setVisibility(GONE);
        mSearchText.setVisibility(VISIBLE);
    }

    public void unExpand() {
        mTitleContent.setVisibility(VISIBLE);
        mSearchText.setVisibility(GONE);
    }

    public boolean isExpand() {
        return mSearchText.getVisibility() == VISIBLE;
    }


    public interface IExpandAction{
        void onExpand();
        void onUnExpand();
    }

}
