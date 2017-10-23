package com.codepath.collabdj.activities;

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
        rlOpenSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch OpenSongsActivity.
                Intent i = new Intent(MainActivity.this, OpenSongsActivity.class);
                startActivity(i);
            }
        });

        RelativeLayout rlJoinSession = (RelativeLayout) findViewById(R.id.rlJoinSession);
        rlJoinSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch JoinSessionActivity.
                Intent i = new Intent(MainActivity.this, JoinSessionActivity.class);
                startActivity(i);
            }
        });

        RelativeLayout rlSharedSongs = (RelativeLayout) findViewById(R.id.rlSharedSongs);
        rlSharedSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch SharedSongsActivity.
                Intent i = new Intent(MainActivity.this, SharedSongsActivity.class);
                startActivity(i);
            }
        });
    }
}
