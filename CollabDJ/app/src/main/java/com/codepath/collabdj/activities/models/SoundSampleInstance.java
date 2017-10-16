package com.codepath.collabdj.activities.models;

import android.content.Context;

import com.codepath.collabdj.sound.SamplePlayer;

import static com.codepath.collabdj.activities.models.SoundSampleInstance.PlayState.LOOPING;
import static com.codepath.collabdj.activities.models.SoundSampleInstance.PlayState.NOT_PLAYING;

/**
 * Created by ilyaseletsky on 10/15/17.
 */

public class SoundSampleInstance {
    SoundSample soundSample;

    public enum PlayState {
        NOT_PLAYING,
        QUEUED,
        LOOPING
    }

    PlayState playState;
    SamplePlayer.SampleHandle sampleHandle;

    public SoundSampleInstance(SoundSample soundSample, SamplePlayer samplePlayer, Context context) {
        this.soundSample = soundSample;
        playState = NOT_PLAYING;

        // Temporary checks to allow creation of dummy samples on recycler view. This checks should go!
        if (soundSample != null && samplePlayer != null) {
            if (soundSample.getPath() != null) {
                sampleHandle = samplePlayer.newSample(soundSample.getPath(), soundSample.getDuration());
            } else {
                sampleHandle = samplePlayer.newSample(context, soundSample.getResourceId(), soundSample.getDuration());
            }
        }
    }

    public SoundSample getSoundSample() {
        return this.soundSample;
    }

    public PlayState getPlayState() {
        return this.playState;
    }

    public void queueSample(long timestamp, int loopAmount) {
        playState = LOOPING;
        sampleHandle.queueSample(timestamp, loopAmount);
    }

    public void stop() {
        playState = NOT_PLAYING;
        sampleHandle.stop();
    }
}
