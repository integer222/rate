package ru.pap.rate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * Created by alex on 13.11.16.
 */

public class SearchView extends FrameLayout {
    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        EditText editText = new EditText(getContext());
        editText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(editText);
    }


}
