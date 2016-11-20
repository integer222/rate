package ru.pap.rate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.pap.rate.R;
import ru.pap.rate.adapters.BaseAdapter;
import ru.pap.rate.adapters.LazyLoadRecyclerScroll;
import ru.pap.rate.db.QuoteContract;
import ru.pap.rate.utils.RRecyclerUtils;

/**
 * Created by alex on 14.11.16.
 */

public abstract class ContentFragment extends BaseFragment implements IFragmentScroll,
        SwipeRefreshLayout.OnRefreshListener,
        LazyLoadRecyclerScroll.ScrollLoadData {

    private SwipeRefreshLayout mSwipe;
    private RecyclerView mRecyclerContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipe.setOnRefreshListener(this);
        mRecyclerContent = (RecyclerView) view.findViewById(R.id.recycler_content);
        mRecyclerContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerContent.setItemAnimator(new DefaultItemAnimator());
        mRecyclerContent.setAdapter(getAdapter());
        mRecyclerContent.addOnScrollListener(new LazyLoadRecyclerScroll(mSwipe, this));
    }

    @Override
    public void onRefresh() {
        loadNewData(getLimit(), true);
    }

    public void setSwipeVisible(final boolean visible){
        if(mSwipe == null){
            return;
        }
        mSwipe.post(new Runnable() {
            @Override
            public void run() {
                mSwipe.setRefreshing(visible);
            }
        });
    }

    public void setSwipeEnabled(boolean enabled){
        if (mSwipe == null){
            return;
        }
        mSwipe.setEnabled(enabled);
    }

    @Override
    public void onScrollUp() {
        RRecyclerUtils.scrollTop(mRecyclerContent);
    }

    public SwipeRefreshLayout getSwipe() {
        return mSwipe;
    }

    public RecyclerView getRecyclerContent() {
        return mRecyclerContent;
    }

    abstract BaseAdapter getAdapter();
    abstract int getLimit();

}
