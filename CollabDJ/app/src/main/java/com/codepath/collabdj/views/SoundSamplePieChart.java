package com.codepath.collabdj.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.codepath.collabdj.CollabDjApplication;
import com.codepath.collabdj.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilyaseletsky on 10/29/17.
 */

public class SoundSamplePieChart extends PieChart {
    PieEntry filledEntry;
    PieEntry emptyEntry;
    PieDataSet dataSet;

    public SoundSamplePieChart(Context context) {
        super(context);

        configurePieChart();
    }

    public SoundSamplePieChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        configurePieChart();
    }

    public SoundSamplePieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        configurePieChart();
    }

    protected void configurePieChart() {
        setTouchEnabled(false);
        getLegend().setEnabled(false);
        setDescription(null);
        setHoleRadius(50.f);
        setTransparentCircleRadius(0.f);
        setHoleColor(Color.TRANSPARENT);

        filledEntry = new PieEntry(0.f);
        emptyEntry = new PieEntry(1.f);

        List<PieEntry> pieEntries = new ArrayList<>(2);
        pieEntries.add(emptyEntry);
        pieEntries.add(filledEntry);

        dataSet = new PieDataSet(pieEntries, null);
        dataSet.setDrawValues(false);

        PieData pieData = new PieData(dataSet);

        setData(pieData);
        invalidate();
    }

    public void setPieChartValue(float value, int colorResource) {
        filledEntry.setY(value);
        emptyEntry.setY(1.f - value);

        dataSet.setColors(new int[] {colorResource, R.color.pieSampleEmpty}, CollabDjApplication.getContext());
        notifyDataSetChanged();
        invalidate();
    }
}
