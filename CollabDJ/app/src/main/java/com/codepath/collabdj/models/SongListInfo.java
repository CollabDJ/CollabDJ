package com.codepath.collabdj.models;

import java.util.Date;

/**
 * Created by ilyaseletsky on 10/22/17.
 */

public class SongListInfo {
    public String title;
    public Date creationDate;
    public String storageLocation;

    public SongListInfo() {

    }

    public SongListInfo(String title,
                        Date creationDate,
                        String storageLocation) {
        this.title = title;
        this.creationDate = creationDate;
        this.storageLocation = storageLocation;
    }
}
