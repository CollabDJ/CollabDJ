package com.codepath.collabdj.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.activities.adapters.SoundSamplesAdapter;
import com.codepath.collabdj.activities.models.SoundSample;
import com.codepath.collabdj.activities.utils.SpacesItemDecoration;
import com.codepath.collabdj.sound.SamplePlayer;

import java.util.ArrayList;
import java.util.List;

public class CreateSongActivity extends AppCompatActivity {

    //Move these constants later
    public static float BEATS_PER_MINUTE = 120;

    public static float BEATS_PER_MEASURE = 4;

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
            SoundSample soundSample = new SoundSample("Sample " + i, 0, "", 0, "");
            testSoundSamples.add(soundSample);
        }

        return testSoundSamples;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO: take this out, for testing only
        SamplePlayer samplePlayer = new SamplePlayer(64);
        SamplePlayer.SampleHandle handle = samplePlayer.newSample(this, R.raw.drum_4_blastcap_start, 8000);
        SamplePlayer.SampleHandle handle0 = samplePlayer.newSample(this, R.raw.drum_4_blastcap_start_0, 8000);
        SamplePlayer.SampleHandle handle1 = samplePlayer.newSample(this, R.raw.drum_4_blastcap_start_1, 8000);
        SamplePlayer.SampleHandle handle2 = samplePlayer.newSample(this, R.raw.drum_8_blastcap, 8000);

        handle.queueSample(SamplePlayer.getCurrentTimestamp() + 8000, 1);
        handle0.queueSample(SamplePlayer.getCurrentTimestamp() + 32000, 1);
        handle1.queueSample(SamplePlayer.getCurrentTimestamp() + 96000, 1);
        handle2.queueSample(SamplePlayer.getCurrentTimestamp() + 150000, 1);
    }
}
