package com.codepath.collabdj.activities.models;

import org.parceler.Parcel;

/**
 * Created by tiago on 10/12/17.
 */

@Parcel
public class SoundSample {

    String name;
    int iconDrawableId;
    String path;
    long duration;
    String pathJsonDefinition;
    boolean isPlaying;

    // Empty constructor needed by the Parcel library.
    public SoundSample() {

    }

    public SoundSample(String name, int iconDrawableId, String path, long duration,
                       String pathJsonDefinition) {
        this.name = name;
        this.iconDrawableId = iconDrawableId;
        this.path = path;
        this.duration = duration;
        this.pathJsonDefinition = pathJsonDefinition;
        this.isPlaying = false;
    }

    public String getName() {
        return this.name;
    }

    public int getIconDrawableId() {
        return this.iconDrawableId;
    }

    public String getPath() {
        return this.path;
    }

    public long getDuration() {
        return this.duration;
    }

    public String getPathJsonDefinition() {
        return  this.pathJsonDefinition;
    }

    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    public void toggleIsPlaying() {
        this.isPlaying = !this.isPlaying;
    }

}
