package com.codepath.collabdj.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.adapters.SharedSongsAdapter;
import com.codepath.collabdj.models.SharedSong;
import com.codepath.collabdj.models.Song;
import com.codepath.collabdj.utils.NearbyConnection;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class OpenSongsActivity extends AppCompatActivity {
    private static final String TAG = "SharedSongsActivity";

    ArrayList<SharedSong> localSongs;
    SharedSongsAdapter localSongsAdapter;
    ListView localsonglvItems;

    private NearbyConnection mNearbyConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_songs);

        mNearbyConnection = new NearbyConnection(this, this);

        localsonglvItems = (ListView) findViewById(R.id.localSongsLV);
        localSongs = getAllSongs();
        localSongsAdapter = new SharedSongsAdapter(this, localSongs);
        localsonglvItems.setAdapter(localSongsAdapter);
        localSongsAdapter.notifyDataSetChanged();

        localsonglvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                playSong(view, localSongs.get(pos).getPathToData());
            }
        });
    }

    @Override
    protected void onStart() {
        if (mNearbyConnection.hasPermissions()) {
            Log.v(TAG, "The app has the required permissions.");
            mNearbyConnection.createGoogleApiClient();
        } else {
            Log.v(TAG, "Requesting the permissions to use NearbyApi.");
            mNearbyConnection.requestPermissions();
        }
        super.onStart();
    }


    public ArrayList<SharedSong> getAllSongs(){
        File dir = getFilesDir();
        ArrayList<SharedSong> localSongs = new ArrayList<SharedSong>();
        File parentFile = new File(dir+"/localsongs/");
        if(parentFile.exists()){
            File[] files = parentFile.listFiles();
            for(File file : files){
                SharedSong song = new SharedSong(file.getName(), null, file.getPath());
                localSongs.add(song);
            }
        }
        return localSongs;
    }

    public void playSong(View view, String path) {
        try {
            File JSONFile = new File(path);

            byte[] JSONBytes = read(JSONFile);

            JSONObject songJson = new JSONObject(new String(JSONBytes));

            Song song = new Song(songJson);
            PlaySongActivity.launch(song, this, view);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This could go into a util class
    public static byte[] read(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException(
                        "EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return buffer;
    }



    /** The user has accepted (or denied) our permission request. */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mNearbyConnection.getRequestCodeRequiredPermissions()) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Log.v(TAG, "Couldn't get the permissions!");
                    finish();
                    return;
                }
            }
            recreate();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
