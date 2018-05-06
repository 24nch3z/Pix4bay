package ru.s4nchez.pix4bay.screens.photolist;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by S4nchez on 19.04.2018.
 */

// Нужен для задания промежутков между изображений (в моём случае)
public class ItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;

    public ItemDecoration(int spanCount, int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        outRect.left = column * spacing / spanCount;
        outRect.right = spacing - (column + 1) * spacing / spanCount;
        if (position >= spanCount) {
            outRect.top = spacing;
        }
    }
}