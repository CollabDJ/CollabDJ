package com.codepath.collabdj.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.codepath.collabdj.CollabDjApplication;
import com.codepath.collabdj.R;
import com.codepath.collabdj.adapters.SoundSamplesAdapter;
import com.codepath.collabdj.utils.SamplePlayer;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

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

    PieEntry filledEntry;
    PieEntry emptyEntry;
    PieDataSet dataSet;

    public void setPiechartValue(float value, int colorResource) {
        filledEntry.setY(value);
        emptyEntry.setY(1.f - value);

        dataSet.setColors(new int[] {colorResource, R.color.pieSampleEmpty}, CollabDjApplication.getContext());
        viewHolder.pcPercent.notifyDataSetChanged();
        viewHolder.pcPercent.invalidate();
    }

    /**
     * Call this in the view holder constructor when things are all getting set up
     */
    public void configurePieChart() {
        viewHolder.pcPercent.setTouchEnabled(false);
        viewHolder.pcPercent.getLegend().setEnabled(false);
        viewHolder.pcPercent.setDescription(null);
        viewHolder.pcPercent.setHoleRadius(90.f);
        viewHolder.pcPercent.setTransparentCircleRadius(0.f);
        viewHolder.pcPercent.setHoleColor(Color.TRANSPARENT);

        filledEntry = new PieEntry(0.f);
        emptyEntry = new PieEntry(1.f);

        List<PieEntry> pieEntries = new ArrayList<>(2);
        pieEntries.add(emptyEntry);
        pieEntries.add(filledEntry);

        dataSet = new PieDataSet(pieEntries, null);
        dataSet.setDrawValues(false);

        PieData pieData = new PieData(dataSet);

        viewHolder.pcPercent.setData(pieData);
        viewHolder.pcPercent.invalidate();
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

            setPiechartValue(percentage, colorResourceValue);

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
