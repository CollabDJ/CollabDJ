package com.codepath.collabdj.models;

import com.codepath.collabdj.R;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final Map<String, SoundSample> SOUND_SAMPLES = initSamplesMap();

    private static final Map<String, SoundSample> initSamplesMap() {
        HashMap<String, SoundSample> res = new HashMap<String, SoundSample>();

        List<SoundSample> soundSamples = new ArrayList<>();

        soundSamples.add(new SoundSample(
                "BlastCap",
                0,
                null,
                R.raw.drum_4_blastcap_start,
                8000,
                null));

        soundSamples.add(new SoundSample(
                "BlastCap 0",
                0,
                null,
                R.raw.drum_4_blastcap_start_0,
                8000,
                null));

        soundSamples.add(new SoundSample(
                "BlastCap 1",
                0,
                null,
                R.raw.drum_4_blastcap_start_1,
                8000,
                null));

        for (SoundSample soundSample : soundSamples) {
            res.put(soundSample.getName(), soundSample);
        }

        return Collections.unmodifiableMap(res);
    }

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
