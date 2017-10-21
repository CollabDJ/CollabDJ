package com.codepath.collabdj.models;

import org.parceler.Parcel;

/**
 * Created by tiago on 10/12/17.
 */

@Parcel
public class SampleUsage {

    int soundSampleIndex;
    long startSection;
    int loopTimes;

    // Empty constructor needed by the Parcel library.
    public SampleUsage() {

    }

    public SampleUsage(int soundSampleIndex, long startSection, int loopTimes) {
        this.soundSampleIndex = soundSampleIndex;
        this.startSection = startSection;
        this.loopTimes = loopTimes;
    }

    public int getSoundSampleIndex() {
        return this.soundSampleIndex;
    }

    public long getStartSection() {
        return this.startSection;
    }

    public int getLoopTimes() {
        return this.loopTimes;
    }
}
