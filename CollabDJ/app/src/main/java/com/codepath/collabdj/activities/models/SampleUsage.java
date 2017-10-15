package com.codepath.collabdj.activities.models;

import org.parceler.Parcel;

/**
 * Created by tiago on 10/12/17.
 */

@Parcel
public class SampleUsage {

    int soundSampleIndex;
    long startTime;
    int loopTime;

    // Empty constructor needed by the Parcel library.
    public SampleUsage() {

    }

    public SampleUsage(int soundSampleIndex, long startTime, int loopTime) {
        this.soundSampleIndex = soundSampleIndex;
        this.startTime = startTime;
        this.loopTime = loopTime;
    }

    public int getSoundSampleIndex() {
        return this.soundSampleIndex;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getLoopTime() {
        return this.loopTime;
    }
}
