package com.codepath.collabdj.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by chenrangong on 10/21/17.
 */

public class SharedSong {
    String title;
    Date createdTime;
    String pathToData;

    public SharedSong(String title, Date createdTime, String pathToData){
        this.title = title;
        this.createdTime = createdTime;
        this.pathToData = pathToData;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getPathToData() {
        return pathToData;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setPathToData(String pathToData) {
        this.pathToData = pathToData;
    }

    public SharedSong(JSONObject jsonObject) throws JSONException{
        this.title = jsonObject.getString("title");
        String createdTime = jsonObject.getString("createdTime");

        this.pathToData = jsonObject.getString("pathToData");
    }
}
