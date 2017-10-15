package com.codepath.collabdj.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.collabdj.R;
import com.codepath.collabdj.sound.SamplePlayer;

public class MainActivity extends AppCompatActivity {

    //Move these constants later
    public static float BEATS_PER_MINUTE = 120;

    public static float BEATS_PER_MEASURE = 4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
