package com.codepath.collabdj.models;

import com.codepath.collabdj.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tiago on 10/12/17.
 */

//@Parcel
public class SoundSample implements Serializable {
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
                R.drawable.ic_mus_drums,
                null,
                R.raw.drum_4_blastcap_start,
                8000,
                null));

        soundSamples.add(new SoundSample(
                "Eighth Gnarler E",
                R.drawable.ic_mus_edm,
                null,
                R.raw.synth_2_eighth_gnarler_e,
                2000,
                null));

        soundSamples.add(new SoundSample(
                "1970 Analog Arp B",
                R.drawable.ic_mus_edm,
                null,
                R.raw.synth_2_1970_analog_arp_b,
                2000,
                null));

        soundSamples.add(new SoundSample(
                "Glass Motion E0",
                R.drawable.ic_notes_glowing,
                null,
                R.raw.synth_4_glass_motion_e0,
                4000,
                null));

        soundSamples.add(new SoundSample(
                "Glass Motion C",
                R.drawable.ic_notes_glowing,
                null,
                R.raw.synth_4_glass_motion_c,
                4000,
                null));

        soundSamples.add(new SoundSample(
                "Airship Rising E1",
                R.drawable.ic_mus_edm,
                null,
                R.raw.synth_4_airship_rising_e1,
                4000,
                null));

        soundSamples.add(new SoundSample(
                "Airship Rising C2",
                R.drawable.ic_mus_edm,
                null,
                R.raw.synth_4_airship_rising_c2,
                4000,
                null));

        soundSamples.add(new SoundSample(
                "Pulsating Chords C1",
                R.drawable.ic_mus_edm,
                null,
                R.raw.synth_4_pulsating_chords_c1,
                4000,
                null));

        soundSamples.add(new SoundSample(
                "Pulsating Chords C2",
                R.drawable.ic_mus_edm,
                null,
                R.raw.synth_4_pulsating_chords_c2,
                4000,
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
