package com.codepath.collabdj.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.collabdj.R;
import com.codepath.collabdj.adapters.SoundSamplesAdapter;
import com.codepath.collabdj.models.SoundSample;
import com.codepath.collabdj.models.SoundSampleInstance;
import com.codepath.collabdj.utils.SpacesItemDecoration;
import com.codepath.collabdj.utils.SamplePlayer;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.collabdj.utils.SamplePlayer.getCurrentTimestamp;

public class CreateSongActivity extends AppCompatActivity implements SoundSamplesAdapter.SoundSamplePlayListener {
    public static int BEATS_PER_MINUTE = 120;
    public static int BEATS_PER_MEASURE = 4;
    public static long MILLISECONDS_PER_MEASURE = 2 * 1000;

    // Tag for logging.
    private static final String TAG = "CreateSongActivity";

    RecyclerView rvSamples;
    List<SoundSampleInstance> mSamples;
    SoundSamplesAdapter mAdapter;

    SamplePlayer samplePlayer;

    long getStartTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_song);

        // Enable up icon.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvSamples = (RecyclerView) findViewById(R.id.rvSamples);
        mSamples = new ArrayList<>();
        mAdapter = new SoundSamplesAdapter(this, mSamples, this);
        rvSamples.setAdapter(mAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvSamples.setLayoutManager(gridLayoutManager);

        // Add dividers to the staggered grid.
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        rvSamples.addItemDecoration(decoration);

        // Set up the sound output
        samplePlayer = new SamplePlayer(64);

        setInitialSoundSamples();
        mAdapter.notifyDataSetChanged();

        //TODO: make it set this when the first sample is played to mark the start of the song
        getStartTimestamp = getCurrentTimestamp();
    }

    private void setInitialSoundSamples() {
        //For now hardcode this
        mSamples.add(new SoundSampleInstance(new SoundSample(
                "BlastCap",
                0,
                null,
                R.raw.drum_4_blastcap_start,
                8000,
                null),
                samplePlayer, this));

        mSamples.add(new SoundSampleInstance(new SoundSample(
                "BlastCap 0",
                0,
                null,
                R.raw.drum_4_blastcap_start_0,
                8000,
                null),
                samplePlayer, this));

        mSamples.add(new SoundSampleInstance(new SoundSample(
                "BlastCap 1",
                0,
                null,
                R.raw.drum_4_blastcap_start_1,
                8000,
                null),
                samplePlayer, this));

        mSamples.add(new SoundSampleInstance(new SoundSample(
                "BlastCap",
                0,
                null,
                R.raw.drum_4_blastcap_start,
                8000,
                null),
                samplePlayer, this));

        mSamples.add(new SoundSampleInstance(new SoundSample(
                "BlastCap",
                0,
                null,
                R.raw.drum_4_blastcap_start,
                8000,
                null),
                samplePlayer, this));
    }

    protected void createEmptyRow() {
        //TODO: call this to set up an empty row of cells that will have the add button
    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO: take this out, for testing only

//        SamplePlayer.SampleHandle handle = samplePlayer.newSample(this, R.raw.drum_4_blastcap_start, 8000);
//        SamplePlayer.SampleHandle handle0 = samplePlayer.newSample(this, R.raw.drum_4_blastcap_start_0, 8000);
//        SamplePlayer.SampleHandle handle1 = samplePlayer.newSample(this, R.raw.drum_4_blastcap_start_1, 8000);
//        SamplePlayer.SampleHandle handle2 = samplePlayer.newSample(this, R.raw.drum_8_blastcap, 8000);
//
//        handle.queueSample(SamplePlayer.getCurrentTimestamp() + 8000, 1);
//        handle0.queueSample(SamplePlayer.getCurrentTimestamp() + 32000, 1);
//        handle1.queueSample(SamplePlayer.getCurrentTimestamp() + 96000, 1);
//        handle2.queueSample(SamplePlayer.getCurrentTimestamp() + 150000, 1);
    }

    public long getCurrentMeasure() {
        long res = (SamplePlayer.getCurrentTimestamp() - getStartTimestamp) / MILLISECONDS_PER_MEASURE;

        Log.v(TAG, "getCurrentMeasure() " + res);

        return res;
    }

    public long getCurrentMeasureTimestamp() {
        long res = getStartTimestamp + (getCurrentMeasure() * MILLISECONDS_PER_MEASURE);

        Log.v(TAG, "getCurrentMeasureTimestamp() " + res);

        return res;
    }

    public long getNextMeasureTimestamp() {
        long res = getCurrentMeasureTimestamp() + MILLISECONDS_PER_MEASURE;

        Log.v(TAG, "getNextMeasureTimestamp() " + res);

        return res;
    }

    @Override
    public void playButtonPressed(SoundSampleInstance soundSampleInstance) {
        //TODO eventually if a stop is queued, make it possible to unstop a loop by resetting its loop count back from 0
        if (soundSampleInstance.getPlayState() == SoundSampleInstance.PlayState.STOPPED) {
            soundSampleInstance.queueSample(getNextMeasureTimestamp(), -1);
        }
        else {
            soundSampleInstance.stop();
        }
    }
}
