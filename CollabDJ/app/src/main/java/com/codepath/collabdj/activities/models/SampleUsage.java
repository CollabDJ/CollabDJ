package com.codepath.collabdj.activities.models;

import org.parceler.Parcel;

/**
 * Created by tiago on 10/12/17.
 */

@Parcel
public class SampleUsage {

    int soundSampleIndex;
    long startTime;
    long endTime;

    // Empty constructor needed by the Parcel library.
    public SampleUsage() {

    }

    public SampleUsage(int soundSampleIndex, long startTime, long endTime) {
        this.soundSampleIndex = soundSampleIndex;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getSoundSampleIndex() {
        return this.soundSampleIndex;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }
}
