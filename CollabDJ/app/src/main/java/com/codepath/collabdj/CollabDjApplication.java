package com.codepath.collabdj;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by tiago on 10/23/17.
 */

public class CollabDjApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
