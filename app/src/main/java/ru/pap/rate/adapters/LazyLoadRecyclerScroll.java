package ru.pap.rate.adapters;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import ru.pap.rate.Config;
import ru.pap.rate.adapters.BaseFooterAdapter;

/**
 * Created by alex on 12.11.16.
 */

public class LazyLoadRecyclerScroll extends RecyclerView.OnScrollListener{
    @Nullable
    private SwipeRefreshLayout mSwipe;
    private ScrollLoadData mScrollLoadData;
    private boolean mLoad = false;
    private int mThreshold;
    private int mLimit = Config.DEFAULT_LOAD_ELEMENT_LIMIT;

    public LazyLoadRecyclerScroll(@Nullable SwipeRefreshLayout mSwipe, ScrollLoadData mScrollLoadData, int mLimit) {
        this.mSwipe = mSwipe;
        this.mScrollLoadData = mScrollLoadData;
        this.mLimit = mLimit;
    }

    public LazyLoadRecyclerScroll(@Nullable SwipeRefreshLayout mSwipe, ScrollLoadData mScrollLoadData) {
        this.mSwipe = mSwipe;
        this.mScrollLoadData = mScrollLoadData;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dx == 0 && dy == 0) {
            return;
        }

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof BaseFooterAdapter && ((BaseFooterAdapter) adapter).useFooter()) {
            return;
        }

        if (mSwipe != null && mSwipe.isRefreshing()) {
            return;
        }
        if (!mScrollLoadData.isMoreAvailable()) {
            return;
        }

        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int visibleItems = recyclerView.getChildCount();
        int allItem = manager.getItemCount();
        int firstVisible = manager.findFirstVisibleItemPosition();

        int delta;
        if (manager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
            delta = dx;
        } else {
            delta = dy;
        }

        if (manager.getReverseLayout() && manager.getStackFromEnd()) {
            delta *= -1;
        }

        if (!mLoad && doLoadCheckLoad(recyclerView, allItem, visibleItems, firstVisible) && delta > 0) {
            mLoad = true;
            mThreshold = firstVisible + visibleItems;
            mScrollLoadData.loadNewData(mLimit, false);
        } else if (mLoad && doCheckUnLoad(recyclerView, allItem, visibleItems, firstVisible, delta)) {
            mLoad = false;
        }
    }

    protected boolean doLoadCheckLoad(RecyclerView recyclerView, int allItem, int visibleItems, int firstVisible) {
        return firstVisible + visibleItems >= allItem;
    }

    protected boolean doCheckUnLoad(RecyclerView recyclerView, int allItem, int visibleItems, int firstVisible) {
        //default state = firstVisible + visibleItems < allItem;
        return doCheckUnLoad(recyclerView, allItem, visibleItems, firstVisible, 0);
    }

    protected boolean doCheckUnLoad(RecyclerView recyclerView, int allItem, int visibleItems, int firstVisible, int delta) {
        if (delta > 0) {
            BaseFooterAdapter adp = (BaseFooterAdapter) recyclerView.getAdapter();
            boolean useFooter = adp.useFooter();
            return (mThreshold + (useFooter ? 1 : 0)) < allItem;
        } else {
            return firstVisible + visibleItems < allItem;
        }
    }

    public void reset() {
        mLoad = false;
    }

    public static interface ScrollLoadData {
        void loadNewData(int limit, boolean refresh);

        boolean isMoreAvailable();
    }
}