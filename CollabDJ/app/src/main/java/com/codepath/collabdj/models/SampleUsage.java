package com.codepath.collabdj.models;

import org.parceler.Parcel;

/**
 * Created by tiago on 10/12/17.
 */

@Parcel
public class SampleUsage {

    int soundSampleIndex;
    long startSection;
    public int loopTimes;       //LOL making this public for easy read/write

    // Empty constructor needed by the Parcel library.
    public SampleUsage() {

    }

    public SampleUsage(int soundSampleIndex, long startSection) {
        this.soundSampleIndex = soundSampleIndex;
        this.startSection = startSection;
    }

    public int getSoundSampleIndex() {
        return this.soundSampleIndex;
    }

    public long getStartSection() {
        return this.startSection;
    }
}
