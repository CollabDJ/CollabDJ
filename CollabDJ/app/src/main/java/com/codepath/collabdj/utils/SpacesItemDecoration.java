package com.codepath.collabdj.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tiago on 10/13/17.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    // Tag for logging.
    private final String TAG = SpacesItemDecoration.class.getName();

    private final int mSpace;

    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    /*
     * Adds margin to each cell of the grid depending its position.
     * The border is achieved by setting the RecyclerView background color to the border color and
     * setting each item background color to white. The margin does the rest.
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // Get the position of this view.
        int position = parent.getChildAdapterPosition(view);

        // Only add margins to elements in the left or right column.
        if (position % 3 != 1) {
            outRect.left = mSpace;
            outRect.right = mSpace;
        }

        // Every cell needs bottom margin.
        outRect.bottom = mSpace;

        // If the element is in the first row, add top margin.
        if (position == 0 || position == 1 || position == 2) {
            outRect.top = mSpace;
        }
    }

}
