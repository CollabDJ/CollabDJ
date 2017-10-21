package com.codepath.collabdj.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 10/12/17.
 */

@Parcel
public class Song {
    /**
     * Sounds can only start playing at the start of a section.
     */
    public static long DEFAULT_MILLISECONDS_PER_SONG_SECTION = 4 * 1000;

    public String title;
    List<String> userNames;
    List<SoundSample> soundSampleList;
    List<SampleUsage> sampleUsageList;
    long numMillisecondsPerSection;

    // Empty constructor needed by the Parcel library.
    public Song() {

    }

    public Song(long numMillisecondsPerSection) {
        this.userNames = new ArrayList<>();
        this.soundSampleList = new ArrayList<>();
        this.sampleUsageList = new ArrayList<>();
        this.numMillisecondsPerSection = numMillisecondsPerSection <= 0
                ? DEFAULT_MILLISECONDS_PER_SONG_SECTION
                : numMillisecondsPerSection;
    }

    public long getNumMillisecondsPerSection() {
        return numMillisecondsPerSection;
    }

    public long getSectionTimestampFromStart(long section) {
        return section * numMillisecondsPerSection;
    }

    public int getNumUserNames() {
        return this.userNames.size();
    }

    public int getNumSoundSamples() {
        return this.soundSampleList.size();
    }

    public int getNumSampleUsages() {
        return this.sampleUsageList.size();
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
