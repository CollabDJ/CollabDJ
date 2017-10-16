package com.codepath.collabdj.activities.models;

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

    public SoundSampleInstance(SoundSample soundSample) {
        this.soundSample = soundSample;
        playState = NOT_PLAYING;
    }

    public SoundSample getSoundSample() {
        return this.soundSample;
    }

    public PlayState getPlayState() {
        return this.playState;
    }

    public void queueSample(long timestamp, int loopAmount) {
        playState = LOOPING;
    }

    public void stop() {
        playState = NOT_PLAYING;
    }
}
