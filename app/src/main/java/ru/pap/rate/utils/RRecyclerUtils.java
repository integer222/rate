package ru.pap.rate.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by alex on 14.11.16.
 */

public class RRecyclerUtils {

    public static final int MAX_SMOOTH_SCROLL_SCREEEN = 2;
    public static final int SMOOTH_DELAY = 10;

    public static void scrollTop(final RecyclerView recyclerView) {
        if (recyclerView == null || recyclerView.getAdapter() == null) {
            return;
        }
        int childCount = recyclerView.getChildCount();
        if (childCount < 1) {
            return;
        }
        int maxSmoth = childCount * MAX_SMOOTH_SCROLL_SCREEEN;
        View view = recyclerView.getChildAt(0);
        if (view == null) {
            return;
        }
        int adapterPosition = recyclerView.getChildAdapterPosition(view);
        if (adapterPosition > maxSmoth) {
            recyclerView.scrollToPosition(childCount);
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(0);
                }
            }, SMOOTH_DELAY);
        } else {
            recyclerView.smoothScrollToPosition(0);
        }
    }
}
