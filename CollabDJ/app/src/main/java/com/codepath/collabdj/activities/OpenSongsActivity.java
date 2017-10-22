package com.codepath.collabdj.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.adapters.SharedSongsAdapter;
import com.codepath.collabdj.models.SharedSong;

import java.util.ArrayList;

public class OpenSongsActivity extends AppCompatActivity {
    private static final String TAG = "SharedSongsActivity";

    ArrayList<SharedSong> sharedSongs;
    SharedSongsAdapter sharedSongsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_songs);

        lvItems = (ListView) findViewById(R.id.localSongsLV);
        sharedSongs = new ArrayList<>();
        sharedSongsAdapter = new SharedSongsAdapter(this, sharedSongs);
        lvItems.setAdapter(sharedSongsAdapter);


    }

    public void getSongs(){

    }
}
