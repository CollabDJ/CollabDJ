package com.codepath.collabdj.activities;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.adapters.SharedSongsAdapter;
import com.codepath.collabdj.models.SharedSong;
import com.codepath.collabdj.models.Song;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

public class SharedSongsActivity extends AppCompatActivity {
    private static final String TAG = "SharedSongsActivity";

    ArrayList<SharedSong> sharedSongs;
    SharedSongsAdapter sharedSongsAdapter;
    ListView lvItems;

    //private StorageReference firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_songs);

        /*firebaseStorage = FirebaseStorage.getInstance().getReference();

        //Firebase storage should be correctly connected to our app id collabdj-1337
        Log.d(TAG, "Firebase storage " + firebaseStorage);*/

        lvItems = (ListView) findViewById(R.id.shareSongsLV);
        sharedSongs = new ArrayList<>();
        sharedSongsAdapter = new SharedSongsAdapter(this, sharedSongs);
        lvItems.setAdapter(sharedSongsAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openSong(view, sharedSongs.get(position));
            }
        });

        String url = "https://collabdj-1337.firebaseio.com/sharedSongs.json";

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    for(Iterator<String> iterator = response.keys(); iterator.hasNext();){
                        String key = iterator.next();
                        sharedSongs.add(new SharedSong(response.getJSONObject(key)));
                    }

                    sharedSongsAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", sharedSongs.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for(int i=0; i<response.length(); i++){
                        sharedSongs.add(new SharedSong(response.getJSONObject(i)));
                    }

                    sharedSongsAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", sharedSongs.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }

    void openSong(final View view, SharedSong sharedSong) {
        StorageReference firebaseStorageRoot = FirebaseStorage.getInstance().getReference();
        StorageReference fileStorage = firebaseStorageRoot.child(sharedSong.getPathToData());

        final long ONE_MEGABYTE = 1024 * 1024;
        fileStorage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));

                    Song song = new Song(jsonObject);

                    PlaySongActivity.launch(song, SharedSongsActivity.this, view);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any error
                exception.printStackTrace();
            }
        });
    }

    @TargetApi(21)
    private void setupSlideTransition() {
        // setup before inflating
        //Transition a = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_right);
        Transition transition = TransitionInflater.from(this).inflateTransition(android.R.transition.fade);
        //transition.setDuration(5000);
        getWindow().setEnterTransition(transition);
    }

}
