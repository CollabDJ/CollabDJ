package com.codepath.collabdj.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.codepath.collabdj.adapters.SoundSamplesAdapter;

/**
 * Created by ilyaseletsky on 10/15/17.
 */

public class SoundSampleView extends RelativeLayout {
    public SoundSamplesAdapter.ViewHolderSample viewHolder;

    public SoundSampleView(Context context) {
        super(context);
    }

    public SoundSampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoundSampleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Update the sound sample state piechart

        //TODO This will eventually be where the piechart gets updated every frame based on sound position
        viewHolder.ibPlayPause.setPlayState(viewHolder.getSoundSampleInstance().getCurrentPlayState());

        super.onDraw(canvas);

        invalidate();
    }
}
