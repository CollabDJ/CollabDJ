package com.codepath.collabdj.utils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.collabdj.R;

/**
 * Created by tiago on 10/13/17.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    // Tag for logging.
    private final String TAG = SpacesItemDecoration.class.getName();

    private final int mSpace;
    private final Drawable mDivider;

    public SpacesItemDecoration(Context context, int space) {
        this.mSpace = space;
        mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
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
/*
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

        int top2 = parent.getPaddingTop();
        int bottom2 = parent.getHeight() - parent.getPaddingBottom();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int left2 = child.getLeft() + params.leftMargin;
            int right2 = left2 + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left2, top2, right2, bottom2);
            mDivider.draw(c);
        }
    }
*/
}
