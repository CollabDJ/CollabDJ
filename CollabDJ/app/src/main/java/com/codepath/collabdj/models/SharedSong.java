package com.codepath.collabdj.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenrangong on 10/21/17.
 */

public class SharedSong implements Serializable {
    String title;
    Date createdTime;
    String pathToDate;

    public SharedSong(String title, Date createdTime, String pathToData){
        this.title = title;
        this.createdTime = createdTime;
        this.pathToDate = pathToData;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getPathToData() {
        return pathToDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setPathToData(String pathToData) {
        this.pathToDate = pathToData;
    }

    public SharedSong(JSONObject jsonObject) throws JSONException{
        this.title = jsonObject.getString("title");
        String time = jsonObject.getJSONObject("createdTime").getString("time");
        long timeLong = Long.parseLong(time);
        this.createdTime = new Date(timeLong);
        this.pathToDate = jsonObject.getString("pathToData");
    }
}
