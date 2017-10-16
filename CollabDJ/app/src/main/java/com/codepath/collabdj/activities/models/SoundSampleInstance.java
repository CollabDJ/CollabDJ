package com.codepath.collabdj.activities.models;

/**
 * Created by ilyaseletsky on 10/15/17.
 */

public class SoundSampleInstance {
    SoundSample soundSample;

    boolean isPlaying;      //TODO: It may be better to make this an enum later to represent the sound sample being queued up to play

    public SoundSampleInstance(SoundSample soundSample) {
        this.soundSample = soundSample;
        isPlaying = false;
    }

    public SoundSample getSoundSample() {
        return this.soundSample;
    }

    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    public void toggleIsPlaying() {
        this.isPlaying = !this.isPlaying;
    }
}
