package com.codepath.collabdj.activities.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.collabdj.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Go straight to the recycler view of samples activity. (For testing purposes).
        Intent i = new Intent(MainActivity.this, CreateSongActivity.class);
        startActivity(i);
    }
}
