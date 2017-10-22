package com.codepath.collabdj.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 10/12/17.
 */

@Parcel
public class Song {
    public static final String FIREBASE_SONG_DATABASE_ROOT = "sharedSongs";
    public static final String FIREBASE_SONG_STORAGE_ROOT = "sharedSongs";
    public static final String LOCAL_SONG_STORAGE_ROOT = "savedSongs";

    public static final String TITLE_JSON_NAME = "title";
    public static final String MILLISECONDS_PER_SECTION_JSON_NAME = "millisecondsPerSection";
    public static final String USER_NAMES_JSON_NAME = "userNames";
    public static final String SOUND_SAMPLES_JSON_NAME = "soundSamples";
    public static final String SOUND_SAMPLE_USAGES_JSON_NAME = "soundSampleUsages";

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

    public JSONObject getJSONObject() {
        JSONObject res = new JSONObject();

        try {
            res.put(TITLE_JSON_NAME, title);
            res.put(MILLISECONDS_PER_SECTION_JSON_NAME, getNumMillisecondsPerSection());

            res.put(USER_NAMES_JSON_NAME, userNames);

            if (!soundSampleList.isEmpty()) {
                JSONArray soundSampleJsonArray = new JSONArray();

                for(SoundSample soundSample : soundSampleList) {
                    soundSampleJsonArray.put(soundSample.getName());
                }

                res.put(SOUND_SAMPLES_JSON_NAME, soundSampleJsonArray);
            }

            JSONArray sampleUsageArray = new JSONArray();

            for(SampleUsage sampleUsage : sampleUsageList) {
                sampleUsageArray.put(sampleUsage.getJSONObject());
            }

            res.put(SOUND_SAMPLE_USAGES_JSON_NAME, sampleUsageArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    public long getNumMillisecondsPerSection() {
        return numMillisecondsPerSection;
    }

    public long getSectionTimestampFromStart(long section) {
        return section * numMillisecondsPerSection;
    }

    public int getNumUserNames() {
        synchronized (this.userNames) {
            return this.userNames.size();
        }
    }

    public int getNumSoundSamples() {
        synchronized (this.soundSampleList) {
            return this.soundSampleList.size();
        }
    }

    public int getNumSampleUsages() {
        synchronized (this.sampleUsageList) {
            return this.sampleUsageList.size();
        }
    }

    public String getUserName(int position) {
        synchronized (this.userNames) {
            return this.userNames.get(position);
        }
    }

    public SoundSample getSoundSample(int position) {
        synchronized (this.soundSampleList) {
            return this.soundSampleList.get(position);
        }
    }

    public SampleUsage getSampleUsage(int position) {
        synchronized (this.sampleUsageList) {
            return this.sampleUsageList.get(position);
        }
    }

    public void addUserName(String userName) {
        synchronized (this.userNames) {
            this.userNames.add(userName);
        }
    }

    public void addSoundSample(SoundSample soundSample) {
        synchronized (this.soundSampleList) {
            this.soundSampleList.add(soundSample);
        }
    }

    public void addSampleUsage(SampleUsage sampleUsage) {
        synchronized (this.sampleUsageList) {
            this.sampleUsageList.add(sampleUsage);
        }
    }

    public String removeUserName(int position) {
        synchronized (this.userNames) {
            return this.userNames.remove(position);
        }
    }

    public SoundSample removeSoundSample(int position) {
        synchronized (this.soundSampleList) {
            return this.soundSampleList.remove(position);
        }
    }

    public SampleUsage removeSampleUsage(int position) {
        synchronized (this.sampleUsageList) {
            return this.sampleUsageList.remove(position);
        }
    }
}
