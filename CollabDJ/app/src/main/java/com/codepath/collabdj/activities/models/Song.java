package com.codepath.collabdj.activities.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 10/12/17.
 */

@Parcel
public class Song {

    public static float BEATS_PER_MINUTE = 120;
    public static float BEATS_PER_MEASURE = 4;

    String title;
    List<String> userNames;
    List<SoundSample> soundSampleList;
    List<SampleUsage> sampleUsageList;

    // Empty constructor needed by the Parcel library.
    public Song() {

    }

    public Song(String title) {
        this.title = title;
        this.userNames = new ArrayList<>();
        this.soundSampleList = new ArrayList<>();
        this.sampleUsageList = new ArrayList<>();
    }

    public String getTitle() {
        return this.title;
    }

    public String getUserName(int position) {
        return this.userNames.get(position);
    }

    public SoundSample getSoundSample(int position) {
        return this.soundSampleList.get(position);
    }

    public SampleUsage getSampleUsage(int position) {
        return this.sampleUsageList.get(position);
    }

    public void addUserName(String userName) {
        this.userNames.add(userName);
    }

    public void addSoundSample(SoundSample soundSample) {
        this.soundSampleList.add(soundSample);
    }

    public void addSampleUsage(SampleUsage sampleUsage) {
        this.sampleUsageList.add(sampleUsage);
    }

    public String removeUserName(int position) {
        return this.userNames.remove(position);
    }

    public SoundSample removeSoundSample(int position) {
        return this.soundSampleList.remove(position);
    }

    public SampleUsage removeSampleUsage(int position) {
        return this.sampleUsageList.remove(position);
    }

}
