package com.codepath.collabdj.views;

import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;

/**
 * Created by ilyaseletsky on 10/29/17.
 */

public class AddSoundSampleView extends PercentRelativeLayout {
    public AddSoundSampleView(Context context) {
        super(context);
    }

    public AddSoundSampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddSoundSampleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //Making the cells be squares
    @Override
    protected void onMeasure(int width, int height) {
        // note we are applying the width value as the height
        super.onMeasure(width, width);
    }
}
