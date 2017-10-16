package com.codepath.collabdj.models;

import android.content.Context;

import com.codepath.collabdj.utils.SamplePlayer;

import static com.codepath.collabdj.models.SoundSampleInstance.PlayState.LOOPING;
import static com.codepath.collabdj.models.SoundSampleInstance.PlayState.STOPPED;

/**
 * Created by ilyaseletsky on 10/15/17.
 */

public class SoundSampleInstance {
    SoundSample soundSample;

    public enum PlayState {
        STOP_QUEUED,
        STOPPED,
        LOOP_QUEUED,
        LOOPING,
    }

    PlayState playState;
    SamplePlayer.SampleHandle sampleHandle;

    public SoundSampleInstance(SoundSample soundSample, SamplePlayer samplePlayer, Context context) {
        this.soundSample = soundSample;
        playState = STOPPED;

        if (soundSample.getPath() != null) {
            sampleHandle = samplePlayer.newSample(soundSample.getPath(), soundSample.getDuration());
        }
        else {
            sampleHandle = samplePlayer.newSample(context, soundSample.getResourceId(), soundSample.getDuration());
        }
    }

    public SoundSample getSoundSample() {
        return this.soundSample;
    }

    public PlayState getPlayState() {
        return this.playState;
    }

    public void queueSample(long timestamp, int loopAmount) {
        playState = LOOPING;        //TODO: this should actually be set to LOOP_QUEUED and eventually the sample state will sync correctly
        sampleHandle.queueSample(timestamp, loopAmount);
    }

    public void stop() {
        playState = STOPPED;        //TODO: this should actually be set to STOP_QUEUED and eventually the sample state will sync correctly
        sampleHandle.stop();
    }
}
