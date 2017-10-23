package com.codepath.collabdj.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.collabdj.R;
import com.codepath.collabdj.adapters.SoundSamplesAdapter;
import com.codepath.collabdj.models.SampleUsage;
import com.codepath.collabdj.models.SharedSong;
import com.codepath.collabdj.models.Song;
import com.codepath.collabdj.models.SoundSample;
import com.codepath.collabdj.models.SoundSampleInstance;
import com.codepath.collabdj.utils.SamplePlayer;
import com.codepath.collabdj.utils.SpacesItemDecoration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codepath.collabdj.models.Song.FIREBASE_SONG_DATABASE_ROOT;
import static com.codepath.collabdj.models.Song.FIREBASE_SONG_STORAGE_ROOT;
import static com.codepath.collabdj.utils.SamplePlayer.PlayInstanceState.STOPPED;
import static com.codepath.collabdj.utils.SamplePlayer.PlayInstanceState.STOP_QUEUED;

public class CreateSongActivity
        extends AppCompatActivity
        implements SoundSamplesAdapter.SoundSamplePlayListener,
            SoundSampleInstance.Listener
{
    public static final int NUM_COLUMNS = 3;

    // Tag for logging.
    private static final String TAG = "CreateSongActivity";

    RecyclerView rvSamples;
    List<SoundSampleInstance> mSamples;
    SoundSamplesAdapter mAdapter;

    SamplePlayer samplePlayer;
    Song song;

    long songStartTimeStamp;

    /**
     * Tracks sample usages that have yet to be written to the song.
     */
    Map<SoundSampleInstance, SampleUsage> activeSampleUsages;

    /**
     * Reverse mapping of SoundSample to SoundSampleIndex in the song.
     */
    Map<SoundSample, Integer> soundSampleIndexMapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_song);

        // Enable up icon.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvSamples = (RecyclerView) findViewById(R.id.rvSamples);
        mSamples = new ArrayList<>();
        mAdapter = new SoundSamplesAdapter(this, mSamples, this);
        rvSamples.setAdapter(mAdapter);
        activeSampleUsages = new HashMap<>();
        soundSampleIndexMapping = new HashMap<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        rvSamples.setLayoutManager(gridLayoutManager);

        // Add dividers to the staggered grid.
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        rvSamples.addItemDecoration(decoration);

        // Set up the sound output
        samplePlayer = new SamplePlayer(64);

        setInitialSoundSamples();
        createInitialEmptyCells();
        mAdapter.notifyDataSetChanged();
    }

    // Creates initial sound samples to test the grid layout.
    private void setInitialSoundSamples() {
        //For now hardcode this

        List<SoundSample> initialSoundSamples = new ArrayList<>();

        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("BlastCap"));
        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("BlastCap 0"));
        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("BlastCap 1"));
        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("BlastCap"));
        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("BlastCap"));

        for (SoundSample soundSample : initialSoundSamples) {
            addSample(soundSample);
        }
    }

    protected void addSample(SoundSample soundSample) {
        soundSampleIndexMapping.put(soundSample, mSamples.size());

        mSamples.add(new SoundSampleInstance(soundSample, samplePlayer, this, this));

        if (song != null) {
            song.addSoundSample(soundSample);
        }
    }

    protected void createInitialEmptyCells() {
        //create the remaining initial cells
        for(int i = 0; i < mSamples.size() % NUM_COLUMNS; ++i) {
            createEmptyCell();
        }

        createEmptyRow();
    }

    protected void createEmptyCell() {
        mSamples.add(new SoundSampleInstance(null, samplePlayer, this, this));
    }

    protected void createEmptyRow() {
        for(int i = 0; i < NUM_COLUMNS; ++i) {
            createEmptyCell();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO: take this out, for testing only

//        SamplePlayer.SampleHandle handle = samplePlayer.newSample(this, R.raw.drum_4_blastcap_start, 8000);
//        SamplePlayer.SampleHandle handle0 = samplePlayer.newSample(this, R.raw.drum_4_blastcap_start_0, 8000);
//        SamplePlayer.SampleHandle handle1 = samplePlayer.newSample(this, R.raw.drum_4_blastcap_start_1, 8000);
//        SamplePlayer.SampleHandle handle2 = samplePlayer.newSample(this, R.raw.drum_8_blastcap, 8000);
//
//        handle.queueSample(SamplePlayer.getCurrentTimestamp() + 8000, 1);
//        handle0.queueSample(SamplePlayer.getCurrentTimestamp() + 32000, 1);
//        handle1.queueSample(SamplePlayer.getCurrentTimestamp() + 96000, 1);
//        handle2.queueSample(SamplePlayer.getCurrentTimestamp() + 150000, 1);
    }

    public long getCurrentSection() {
        if (song == null) {
            Log.v(TAG, "getCurrentSection() called on song that hasn't started yet.  Returning 0.");

            return 0;
        }

        long res = (SamplePlayer.getCurrentTimestamp() - songStartTimeStamp) / song.getNumMillisecondsPerSection();

        Log.v(TAG, "getCurrentSection() " + res);

        return res;
    }

    public long getSectionTimestamp(long section) {
        if (song == null) {
            Log.v(TAG, "getSectionTimestamp() called on song that hasn't started yet.  Returning 0.");

            return 0;
        }

        long res = songStartTimeStamp + (song.getSectionTimestampFromStart(section));

        Log.v(TAG, "getCurrentSectionTimestamp() " + res);

        return res;
    }

    public void setupNewSong() {
        song = new Song(0);     //It could eventually be possible to set length per section in settings before starting a song
        songStartTimeStamp = SamplePlayer.getCurrentTimestamp();

        //Go over every SoundSampleInstance already added
        for(SoundSampleInstance soundSampleInstance : mSamples) {
            if(soundSampleInstance.getSoundSample() != null) {
                song.addSoundSample(soundSampleInstance.getSoundSample());
            }
        }
    }

    public UploadTask saveSongToCloud(String songName) {
        if (song == null) {
            return null;
        }

        song.title = songName;

        JSONObject jsonObject = song.getJSONObject();

        DatabaseReference firebaseDatabaseRoot = FirebaseDatabase.getInstance().getReference();
        DatabaseReference songDatabase = firebaseDatabaseRoot.child(FIREBASE_SONG_DATABASE_ROOT);

        DatabaseReference newSongRef = songDatabase.push();

        String songPath = FIREBASE_SONG_STORAGE_ROOT + "/" + newSongRef.getKey();

        newSongRef.setValue(new SharedSong(song.title, new Date(), songPath));

        StorageReference firebaseStorageRoot = FirebaseStorage.getInstance().getReference();
        StorageReference fileStorage = firebaseStorageRoot.child(songPath);
        return fileStorage.putBytes(jsonObject.toString().getBytes());
    }

    public void saveSongLocally(String filename) {
        if (song == null) {
            return;
        }

        JSONObject jsonObject = song.getJSONObject();

        try {
            File dir = getFilesDir();
            File file = new File(dir + "/localsongs/", filename);
            if(file.getParentFile().mkdirs()){
                file.createNewFile();
                FileOutputStream outputFile = new FileOutputStream(file);
                outputFile.write(jsonObject.toString().getBytes());
                outputFile.flush();
                outputFile.close();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //TODO: for now save the song to cloud, later show an alert dialog asking if the user wants to quit without saving

        saveSongLocally("Test");
        UploadTask uploadTask = saveSongToCloud("Test");

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    @Override
    public long playButtonPressed(SoundSampleInstance soundSampleInstance) {
        if(song == null) {
            setupNewSong();
        }

        SamplePlayer.SampleHandle.PlayInstance playInstance = soundSampleInstance.getCurrentPlayInstance();

        if (playInstance == null || playInstance.getPlayState() == STOPPED) {
            soundSampleInstance.queueSample(getCurrentSection() + 1, song.getNumMillisecondsPerSection(), songStartTimeStamp, -1);
        }
        else if (playInstance.getPlayState() == STOP_QUEUED) {
            playInstance.setLoopAmount(-1);
        }
        else {
            soundSampleInstance.stop();
        }

        return song.getNumMillisecondsPerSection();
    }

    @Override
    public void startPlaying(SoundSampleInstance soundSampleInstance, long startSection) {
        synchronized (activeSampleUsages) {
            SampleUsage sampleUsage = new SampleUsage(soundSampleIndexMapping.get(soundSampleInstance.getSoundSample()), startSection);

            activeSampleUsages.put(soundSampleInstance, sampleUsage);
        }
    }

    @Override
    public void stopPlaying(SoundSampleInstance soundSampleInstance, int numTimesPlayed) {
        SampleUsage sampleUsage = null;

        synchronized (activeSampleUsages) {
            sampleUsage = activeSampleUsages.get(soundSampleInstance);
            activeSampleUsages.remove(soundSampleInstance);
        }

        sampleUsage.loopTimes = numTimesPlayed;

        song.addSampleUsage(sampleUsage);
    }
}
