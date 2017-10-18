package com.chandlersystem.chandler.custom_views;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class CategoryDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private int mColumnCount;

    public CategoryDecoration(int space, int columnCount) {
        this.mSpace = space;
        this.mColumnCount = columnCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % mColumnCount;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        // Calculate this layout for grid layout manager
        if (!(layoutManager instanceof GridLayoutManager)) {
            throw new RuntimeException("This layout is only accepted for GridLayoutManger");
        }

        outRect.left = mSpace - column * mSpace / mColumnCount; // spacing - column * ((1f / spanCount) * spacing)
        outRect.right = (column + 1) * mSpace / mColumnCount; // (column + 1) * ((1f / spanCount) * spacing)

        if (position < mColumnCount) { // top edge
            outRect.top = mSpace;
        }
        outRect.bottom = mSpace;
    }
}