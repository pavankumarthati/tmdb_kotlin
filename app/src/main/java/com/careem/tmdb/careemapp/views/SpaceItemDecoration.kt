package com.careem.tmdb.careemapp.views

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpaceItemDecoration(val widthInPixels: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) : Unit {
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.set(widthInPixels, widthInPixels, widthInPixels, widthInPixels);
        } else {
            outRect.set(widthInPixels, widthInPixels, widthInPixels, 0);
        }
    }
}