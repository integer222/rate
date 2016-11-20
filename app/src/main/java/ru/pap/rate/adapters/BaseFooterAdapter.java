package ru.pap.rate.adapters;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.pap.rate.R;
import ru.pap.rate.holders.ProgressHolder;

/**
 * Created by alex on 12.11.16.
 */

public abstract class BaseFooterAdapter<T extends RecyclerView.ViewHolder> extends BaseAdapter<T> {



    public enum AdapterState {
        VIEW,
        LOAD_FOOTER,
        LOAD_HEADER
    }

    public final static int VIEW_ITEM = -1;
    public final static int VIEW_FOOTER = -2;
    public final static int VIEW_HEADER = -3;

    protected AdapterState mAdapterState = AdapterState.VIEW;

    public BaseFooterAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder fillHolder;
        if (viewType == VIEW_FOOTER || viewType == VIEW_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_load, parent, false);
            fillHolder = new ProgressHolder(view);
        } else {
            fillHolder = onCreateItemViewHolder(parent, viewType);
        }
        return (T) fillHolder;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        if (!(holder instanceof ProgressHolder)) {
            super.onBindViewHolder(holder, position);
        }
    }

    public abstract T onCreateItemViewHolder(ViewGroup parent, int viewType);

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && useHeader()) {
            return VIEW_HEADER;
        } else {
            return super.getItemCount() <= position && useFooter() ? VIEW_FOOTER : VIEW_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        count = (useFooter() || useHeader()) ? count + 1 : count;
        return count;
    }

    @Override
    public long getItemId(int position) {
        if (useHeader()) {
            position++;
        }
        return super.getItemId(position);
    }

    public AdapterState getAdapterState() {
        return mAdapterState;
    }


    public void setAdapterState(AdapterState mAdapterState) {
        if (this.getAdapterState() != mAdapterState) {
            if (mAdapterState == AdapterState.LOAD_FOOTER) {
                this.mAdapterState = mAdapterState;
                notifyItemInserted(getItemCount());
            } else if (mAdapterState == AdapterState.LOAD_HEADER) {
                this.mAdapterState = mAdapterState;
                notifyItemInserted(0);
            } else {
                //remove loading view
                int position;
                if (this.getAdapterState() == AdapterState.LOAD_FOOTER) {
                    notifyItemRemoved(getItemCount());
                } else {
                    notifyDataSetChanged();
//                    notifyItemRemoved(0);
                }
                this.mAdapterState = mAdapterState;

            }
        }
    }

    public boolean isEmpty() {
        return getCursor() == null || getCursor().getCount() == 0;
    }

    public boolean useFooter() {
        return mAdapterState.equals(AdapterState.LOAD_FOOTER);
    }

    public boolean useHeader() {
        return mAdapterState.equals(AdapterState.LOAD_HEADER);
    }




}
