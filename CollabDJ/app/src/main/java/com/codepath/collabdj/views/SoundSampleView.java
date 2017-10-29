package com.codepath.collabdj.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.codepath.collabdj.R;
import com.codepath.collabdj.adapters.SoundSamplesAdapter;
import com.codepath.collabdj.utils.SamplePlayer;

import static com.codepath.collabdj.utils.SamplePlayer.PlayInstanceState.LOOP_QUEUED;

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

    //Making the samples be squares
    @Override
    protected void onMeasure(int width, int height) {
        // note we are applying the width value as the height
        super.onMeasure(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Update the sound sample state piechart

        viewHolder.pbLoadingIndicator.setVisibility(viewHolder.getSoundSampleInstance().isLoaded() ? INVISIBLE : VISIBLE);

        SamplePlayer.SampleHandle.PlayInstance playInstance = viewHolder.getSoundSampleInstance().getCurrentPlayInstance();

        if (playInstance == null) {
            viewHolder.pcPercent.setVisibility(INVISIBLE);
            viewHolder.ivPlayPause.setImageResource(R.drawable.ic_play);
        }
        else {
            viewHolder.pcPercent.setVisibility(VISIBLE);

            float percentage = (float) playInstance.getRemainingDelay()
                    / (playInstance.getPlayState() == LOOP_QUEUED
                        ? viewHolder.millisecondsPerSection
                        : (float) viewHolder.getSoundSampleInstance().getSoundSample().getDuration());

            int colorResourceValue = 0;

            switch(playInstance.getPlayState()) {
                case LOOP_QUEUED:
                    colorResourceValue = R.color.pieSampleLoopQueued;
                    break;

                case LOOPING:
                    colorResourceValue = R.color.pieSampleLooping;
                    break;

                case STOP_QUEUED:
                    colorResourceValue = R.color.pieSampleStopQueued;
                    break;
            }

            viewHolder.pcPercent.setPieChartValue(percentage, colorResourceValue);

            switch(playInstance.getPlayState()) {
                case STOP_QUEUED:
                case STOPPED:
                    viewHolder.ivPlayPause.setImageResource(R.drawable.ic_play);
                    break;
                default:
                    viewHolder.ivPlayPause.setImageResource(R.drawable.ic_pause);
                    break;
            }
        }

        super.onDraw(canvas);

        invalidate();
    }
}
