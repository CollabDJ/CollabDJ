package com.codepath.collabdj.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by chenrangong on 10/21/17.
 */

public class SharedSong {
    String title;
    Date creationDate;
    String storageLocation;

    public SharedSong(String title, Date createdTime, String pathToData){
        this.title = title;
        this.creationDate = createdTime;
        this.storageLocation = pathToData;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreatedTime() {
        return creationDate;
    }

    public String getPathToData() {
        return storageLocation;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedTime(Date createdTime) {
        this.creationDate = createdTime;
    }

    public void setPathToData(String pathToData) {
        this.storageLocation = pathToData;
    }

    public SharedSong(JSONObject jsonObject) throws JSONException{
        this.title = jsonObject.getString("title");
        String time = jsonObject.getJSONObject("creationDate").getString("time");
        long timeLong = Long.parseLong(time);
        this.creationDate = new Date(timeLong);
        this.storageLocation = jsonObject.getString("storageLocation");
    }
}
