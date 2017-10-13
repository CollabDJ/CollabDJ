package com.codepath.collabdj.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.codepath.collabdj.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attach click listeners to each RelativeLayout.
        setupClickListeners();
    }

    private void setupClickListeners() {

        RelativeLayout rlNewSong = (RelativeLayout) findViewById(R.id.rlNewSong);
        rlNewSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch CreateSongActivity.
                Intent i = new Intent(MainActivity.this, CreateSongActivity.class);
                startActivity(i);
            }
        });

        RelativeLayout rlOpenSong = (RelativeLayout) findViewById(R.id.rlOpenSong);
        RelativeLayout rlJoinSession = (RelativeLayout) findViewById(R.id.rlJoinSession);
        RelativeLayout rlSharedSongs = (RelativeLayout) findViewById(R.id.rlSharedSongs);

    }
}
