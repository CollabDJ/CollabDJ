package com.codepath.collabdj.activities.models;

import org.parceler.Parcel;

/**
 * Created by tiago on 10/12/17.
 */

@Parcel
public class User {

    String name;

    // Empty constructor needed by the Parcel library.
    public User() {

    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
