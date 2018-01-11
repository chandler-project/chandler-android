package com.chandlersystem.chandler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerViewWithEmptyState extends RecyclerView {
    private View emptyView;

    public RecyclerViewWithEmptyState(Context context) {
        super(context);
    }

    public RecyclerViewWithEmptyState(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewWithEmptyState(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            updateUI(adapter);
        }
    };

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        setVisibility(View.INVISIBLE);
    }

    private void updateUI(Adapter adapter) {
        if (adapter != null && emptyView != null) {
            if (adapter.getItemCount() == 0) {
                emptyView.setVisibility(View.VISIBLE);
                setVisibility(View.INVISIBLE);
            } else {
                emptyView.setVisibility(View.INVISIBLE);
                setVisibility(View.VISIBLE);
            }
        }
    }
}