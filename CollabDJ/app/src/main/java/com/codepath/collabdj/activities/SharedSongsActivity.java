package com.codepath.collabdj.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.adapters.SharedSongsAdapter;
import com.codepath.collabdj.models.SharedSong;
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

        String url = "https://collabdj-1337.firebaseio.com/songs.json";

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
}
