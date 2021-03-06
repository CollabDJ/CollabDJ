package com.codepath.collabdj.activities;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.codepath.collabdj.R;
import com.codepath.collabdj.utils.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup explode transition.
        setupExplodeTransition();
        setContentView(R.layout.activity_main);

        // Attach click listeners to each RelativeLayout.
        setupClickListeners();
        setupAnimations();
    }

    private void setupClickListeners() {

        RelativeLayout rlNewSong = (RelativeLayout) findViewById(R.id.rlNewSong);
        rlNewSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch CreateSongActivity.
                Intent i = new Intent(MainActivity.this, CreateSongActivity.class);
                // options need to be passed when starting the activity
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                startActivity(i, options.toBundle());
            }
        });

        RelativeLayout rlOpenSong = (RelativeLayout) findViewById(R.id.rlOpenSong);
        rlOpenSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch OpenSongsActivity.
                Intent i = new Intent(MainActivity.this, OpenSongsActivity.class);
                // options need to be passed when starting the activity
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                startActivity(i, options.toBundle());
            }
        });

        RelativeLayout rlJoinSession = (RelativeLayout) findViewById(R.id.rlJoinSession);
        rlJoinSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch OpenSongsActivity.
                Intent i = new Intent(MainActivity.this, CreateSongActivity.class);
                i.putExtra(CreateSongActivity.IS_HOST, false);
                // options need to be passed when starting the activity
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                startActivity(i, options.toBundle());
            }
        });

        RelativeLayout rlSharedSongs = (RelativeLayout) findViewById(R.id.rlSharedSongs);
        rlSharedSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch SharedSongsActivity.
                Intent i = new Intent(MainActivity.this, SharedSongsActivity.class);
                // options need to be passed when starting the activity
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                startActivity(i, options.toBundle());

                //For now hardcoding this to download a file from firebase
//                StorageReference firebaseStorageRoot = FirebaseStorage.getInstance().getReference();
//                StorageReference fileStorage = firebaseStorageRoot.child("sharedSongs").child("-Kx6-90iwpzjjQRzIGhU");
//
//                final long ONE_MEGABYTE = 1024 * 1024;
//                fileStorage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(new String(bytes));
//
//                            Song song = new Song(jsonObject);
//
//                            // Data is returned in bytes array
//
//                            Intent i = new Intent(MainActivity.this, PlaySongActivity.class);
//                            i.putExtra(PlaySongActivity.SONG_KEY, song);
//
//                            startActivity(i);
//                        }
//                        catch(Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle any error
//                        exception.printStackTrace();
//                    }
//                });
            }
        });
    }

    private void setupAnimations() {
        setupPulsatingAnimation(findViewById(R.id.ivNewSong));
        setupPulsatingAnimation(findViewById(R.id.tvNewSong));

        setupPulsatingAnimation(findViewById(R.id.ivOpenSong));
        setupPulsatingAnimation(findViewById(R.id.tvOpenSong));

        setupPulsatingAnimation(findViewById(R.id.ivJoinSession));
        setupPulsatingAnimation(findViewById(R.id.tvJoinSession));

        setupPulsatingAnimation(findViewById(R.id.ivSharedSongs));
        setupPulsatingAnimation(findViewById(R.id.tvSharedSongs));
    }

    private void setupPulsatingAnimation(View view)
    {
        AnimationUtils.setupPulsatingAnimation(view, 500, ValueAnimator.INFINITE, 1.0f, 1.05f);
    }

    @TargetApi(21)
    private void setupExplodeTransition() {
        Transition transition = TransitionInflater.from(this).inflateTransition(android.R.transition.explode);
        transition.setDuration(500);
        getWindow().setExitTransition(transition);

    }
}
