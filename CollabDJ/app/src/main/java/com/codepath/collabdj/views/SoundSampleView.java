package com.codepath.collabdj.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.codepath.collabdj.adapters.SoundSamplesAdapter;
import com.codepath.collabdj.utils.SamplePlayer;

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

        SamplePlayer.SampleHandle.PlayInstance playInstance = viewHolder.getSoundSampleInstance().getCurrentPlayInstance();

        if (playInstance == null) {
            viewHolder.tvPercent.setVisibility(INVISIBLE);
            viewHolder.ibPlayPause.setPlayState(SamplePlayer.PlayInstanceState.STOPPED);
        }
        else {
            viewHolder.tvPercent.setVisibility(VISIBLE);
            viewHolder.tvPercent.setText("" + (1.0f - ((float) playInstance.getRemainingDelay()
                    / (float) viewHolder.getSoundSampleInstance().getSoundSample().getDuration())));      //Hacked together for now, replace with pie chart

            switch(playInstance.getPlayState()) {
                case LOOP_QUEUED:
                    viewHolder.tvPercent.setTextColor(Color.YELLOW);
                    break;

                case LOOPING:
                    viewHolder.tvPercent.setTextColor(Color.GREEN);
                    break;

                case STOP_QUEUED:
                    viewHolder.tvPercent.setTextColor(Color.RED);
                    break;
            }

            viewHolder.ibPlayPause.setPlayState(playInstance.getPlayState());
        }

        super.onDraw(canvas);

        invalidate();
    }
}
