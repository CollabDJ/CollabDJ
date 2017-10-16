package com.codepath.collabdj.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.collabdj.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SharedSongsActivity extends AppCompatActivity {
    private static final String TAG = "SharedSongsActivity";

    private StorageReference firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_songs);

        firebaseStorage = FirebaseStorage.getInstance().getReference();

        //Firebase storage should be correctly connected to our app id collabdj-1337
        Log.d(TAG, "Firebase storage " + firebaseStorage);
    }
}
