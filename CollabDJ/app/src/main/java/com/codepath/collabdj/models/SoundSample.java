package com.codepath.collabdj.models;

import org.parceler.Parcel;

/**
 * Created by tiago on 10/12/17.
 */

@Parcel
public class SoundSample {
    String name;
    int iconDrawableId;
    String path;
    int resourceId;
    long duration;
    String pathJsonDefinition;

    // Empty constructor needed by the Parcel library.
    public SoundSample() {

    }

    public SoundSample(String name,
                       int iconDrawableId,
                       String path,
                       int resourceId,
                       long duration,
                       String pathJsonDefinition) {
        this.name = name;
        this.iconDrawableId = iconDrawableId;
        this.path = path;
        this.resourceId = resourceId;
        this.duration = duration;
        this.pathJsonDefinition = pathJsonDefinition;
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

    public int getResourceId() {
        return this.resourceId;
    }

    public long getDuration() {
        return this.duration;
    }

    public String getPathJsonDefinition() {
        return this.pathJsonDefinition;
    }
}
