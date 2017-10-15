package com.codepath.collabdj.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.activities.adapters.SoundSamplesAdapter;
import com.codepath.collabdj.activities.models.SoundSample;
import com.codepath.collabdj.activities.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CreateSongActivity extends AppCompatActivity {

    // Tag for logging.
    private final String TAG = CreateSongActivity.class.getName();

    RecyclerView rvSamples;
    List<SoundSample> mSamples;
    SoundSamplesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_song);

        // Enable up icon.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvSamples = (RecyclerView) findViewById(R.id.rvSamples);
        mSamples = new ArrayList<>();
        mAdapter = new SoundSamplesAdapter(this, mSamples);
        rvSamples.setAdapter(mAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvSamples.setLayoutManager(gridLayoutManager);

        // Add dividers to the staggered grid.
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        rvSamples.addItemDecoration(decoration);

        // Add the fake sound samples.
        mSamples.addAll(getSoundSamples());
        mAdapter.notifyDataSetChanged();

    }

    // Creates 50 fake sound samples to test the grid layout.
    private List<SoundSample> getSoundSamples() {
        ArrayList<SoundSample> testSoundSamples = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            SoundSample soundSample = new SoundSample("Sample " + i, 0, "", 0.0, "");
            testSoundSamples.add(soundSample);
        }

        return testSoundSamples;
    }
}
