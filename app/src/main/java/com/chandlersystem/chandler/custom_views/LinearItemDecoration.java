package com.chandlersystem.chandler.custom_views;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;

    public LinearItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        if (itemPosition == 0) {
            outRect.top = mSpace;
        } else {
            outRect.top = 0;
        }

        outRect.bottom = mSpace;
    }
}