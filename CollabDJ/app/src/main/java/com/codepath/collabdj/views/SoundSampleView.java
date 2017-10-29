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

        dataSet.setColors(new int[] {R.color.pieSampleEmpty, colorResource}, CollabDjApplication.getContext());
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
        viewHolder.pcPercent.setDrawHoleEnabled(false);
        viewHolder.pcPercent.setTransparentCircleRadius(0.f);

        filledEntry = new PieEntry(0.f);
        emptyEntry = new PieEntry(1.f);

        List<PieEntry> pieEntries = new ArrayList<>(2);
        pieEntries.add(filledEntry);
        pieEntries.add(emptyEntry);

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
            viewHolder.tvPercent.setVisibility(INVISIBLE);
            viewHolder.pcPercent.setVisibility(INVISIBLE);
            viewHolder.ivPlayPause.setImageResource(android.R.drawable.ic_media_play);
        }
        else {
            viewHolder.tvPercent.setVisibility(VISIBLE);
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

            //Hacked together for now, replace with pie chart
            viewHolder.tvPercent.setText(String.format("%1.2f", 1.0f - percentage));

            switch(playInstance.getPlayState()) {
                case LOOP_QUEUED:
                    viewHolder.tvPercent.setTextColor(Color.BLUE);
                    break;

                case LOOPING:
                    viewHolder.tvPercent.setTextColor(Color.GREEN);
                    break;

                case STOP_QUEUED:
                    viewHolder.tvPercent.setTextColor(Color.RED);
                    break;
            }

            switch(playInstance.getPlayState()) {
                case STOP_QUEUED:
                case STOPPED:
                    viewHolder.ivPlayPause.setImageResource(android.R.drawable.ic_media_play);
                    break;
                default:
                    viewHolder.ivPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    break;
            }
        }

        super.onDraw(canvas);

        invalidate();
    }
}
