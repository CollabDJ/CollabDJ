package com.codepath.collabdj.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.collabdj.R;
import com.codepath.collabdj.models.SampleUsage;
import com.codepath.collabdj.models.Song;
import com.codepath.collabdj.models.SoundSampleInstance;
import com.codepath.collabdj.utils.SamplePlayer;

import java.util.ArrayList;
import java.util.List;

public class PlaySongActivity extends AppCompatActivity {
    public static final String SONG_KEY = "song";

    Song song;
    SamplePlayer samplePlayer;

    List<SoundSampleInstance> soundSampleInstances;
    long songStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        song = (Song) getIntent().getSerializableExtra(SONG_KEY);

        samplePlayer = new SamplePlayer(64);

        //Add the samples
        soundSampleInstances = new ArrayList<>(song.getNumSoundSamples());

        for(int i = 0; i < song.getNumSoundSamples(); ++i) {
            soundSampleInstances.add(new SoundSampleInstance(song.getSoundSample(i), samplePlayer, this, null));
        }

        songStartTime = SamplePlayer.getCurrentTimestamp();

        //Queue up the samples so the song starts playing
        for(int i = 0; i < song.getNumSampleUsages(); ++i) {
            SampleUsage sampleUsage = song.getSampleUsage(i);

            soundSampleInstances.get(sampleUsage.getSoundSampleIndex()).queueSample(sampleUsage.getStartSection(),
                    song.getNumMillisecondsPerSection(),
                    songStartTime,
                    sampleUsage.loopTimes);
        }
    }
}
