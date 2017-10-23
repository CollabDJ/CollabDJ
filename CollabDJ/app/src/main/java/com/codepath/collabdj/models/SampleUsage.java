package com.codepath.collabdj.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by tiago on 10/12/17.
 */

//@Parcel
public class SampleUsage implements Serializable {

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

    public SampleUsage(JSONObject jsonObject) throws JSONException {
        soundSampleIndex = jsonObject.getInt("soundSampleIndex");
        startSection = jsonObject.getLong("startSection");
        loopTimes = jsonObject.getInt("loopTimes");;
    }

    public int getSoundSampleIndex() {
        return this.soundSampleIndex;
    }

    public long getStartSection() {
        return this.startSection;
    }

    public JSONObject getJSONObject() {
        JSONObject res = new JSONObject();

        try {
            res.put("soundSampleIndex", soundSampleIndex);
            res.put("startSection", startSection);
            res.put("loopTimes", loopTimes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }
}
