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

        RelativeLayout rlSharedSongs = (RelativeLayout) findViewById(R.id.rlSharedSongs);
        rlSharedSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch SharedSongsActivity.
                Intent i = new Intent(MainActivity.this, SharedSongsActivity.class);
                startActivity(i);

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
}
