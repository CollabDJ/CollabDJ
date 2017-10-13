package com.codepath.collabdj.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.collabdj.R;
import com.codepath.collabdj.sound.SamplePlayer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: take this out, for testing only
        SamplePlayer samplePlayer = new SamplePlayer(64);
        SamplePlayer.SampleHandle handle = samplePlayer.newSample(this, R.raw.drum_8_blastcap);

        samplePlayer.queueSample(handle, 0, 100);
    }
}
