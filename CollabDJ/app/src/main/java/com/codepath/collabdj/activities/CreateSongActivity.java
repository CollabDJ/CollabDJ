package com.codepath.collabdj.activities;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codepath.collabdj.R;
import com.codepath.collabdj.adapters.SoundSamplesAdapter;
import com.codepath.collabdj.fragments.AddSoundSampleDialogFragment;
import com.codepath.collabdj.models.SampleUsage;
import com.codepath.collabdj.models.SharedSong;
import com.codepath.collabdj.models.Song;
import com.codepath.collabdj.models.SoundSample;
import com.codepath.collabdj.models.SoundSampleInstance;
import com.codepath.collabdj.utils.NearbyConnection;
import com.codepath.collabdj.utils.PixelUtils;
import com.codepath.collabdj.utils.SamplePlayer;
import com.codepath.collabdj.utils.SpacesItemDecoration;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
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
            SoundSampleInstance.Listener, AddSoundSampleDialogFragment.AddSoundSampleDialogListener
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
    Map<SoundSampleInstance, Integer> soundSampleIndexMapping;

    /**
     * Hamburger menu references.
     */
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private NearbyConnection mNearbyConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_song);

        // Setup the nearby connections.
        setupNearbyConnection();

        // Set a toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Find our drawer view.
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle.
        mDrawer.addDrawerListener(drawerToggle);

        // Find our navigationView.
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view.
        setupDrawerView(nvDrawer);

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
        SpacesItemDecoration decoration = new SpacesItemDecoration(this, 5);
        rvSamples.addItemDecoration(decoration);

        // Set up the sound output
        samplePlayer = new SamplePlayer(64);

        setInitialSoundSamples();
        createInitialEmptyCells();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        samplePlayer.kill();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Creates initial sound samples to test the grid layout.
    private void setInitialSoundSamples() {
        //For now hardcode this

        List<SoundSample> initialSoundSamples = new ArrayList<>();

        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("BlastCap"));
        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("Pulsating Chords C1"));
        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("Glass Motion E0"));
        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("Eighth Gnarler E"));

//        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("BlastCap"));
//        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("1970 Analog Arp B"));
//        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("Glass Motion E0"));
//        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("Glass Motion C"));
//        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("Airship Rising E1"));
//        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("Airship Rising C2"));
//
//        initialSoundSamples.add(SoundSample.SOUND_SAMPLES.get("Pulsating Chords C2"));

        for (SoundSample soundSample : initialSoundSamples) {
            addSample(soundSample);
        }
    }

    protected void addSample(SoundSample soundSample) {
        SoundSampleInstance soundSampleInstance = new SoundSampleInstance(soundSample, samplePlayer, this, this);

        soundSampleIndexMapping.put(soundSampleInstance, mSamples.size());
        mSamples.add(soundSampleInstance);

        if (song != null) {
            song.addSoundSample(soundSample);
        }
    }

    /**
     * Called by the add sound sample dialog fragment
     * @param soundSample
     */
    public void onAddNewSample(SoundSample soundSample) {
        int firstEmptyCellIndex = findFirstEmptyCell();

        //If for some reason didn't find an empty cell just add one
        if (firstEmptyCellIndex < 0) {
            addSample(soundSample);
            createInitialEmptyCells();
        }
        else {
            SoundSampleInstance soundSampleInstance = new SoundSampleInstance(soundSample, samplePlayer, this, this);

            mSamples.set(firstEmptyCellIndex, soundSampleInstance);
            soundSampleIndexMapping.put(soundSampleInstance, firstEmptyCellIndex);

            if (song != null) {
                song.addSoundSample(soundSample);
            }

            //check if we need a new empty row
            int numEmptyCells = mSamples.size() - firstEmptyCellIndex - 1;

            if (numEmptyCells < NUM_COLUMNS * 2) {
                createEmptyRow();
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    protected int findFirstEmptyCell() {
        int firstEmptyCellIndex = -1;

        //Find the first empty cell by looking from the back
        for(int i = mSamples.size() - 1; i >= 0; --i) {
            if (mSamples.get(i).getSoundSample() == null) {
                firstEmptyCellIndex = i;
            }
            else {
                break;
            }
        }

        return firstEmptyCellIndex;
    }

    protected void createInitialEmptyCells() {
        //create the remaining initial cells
        for(int i = 0; i < mSamples.size() % NUM_COLUMNS; ++i) {
            createEmptyCell();
        }

        createEmptyRow();
        createEmptyRow();
        createEmptyRow();
        createEmptyRow();
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
        if (mNearbyConnection.hasPermissions()) {
            Log.v(TAG, "The app has the required permissions.");
            mNearbyConnection.createGoogleApiClient();
        } else {
            Log.v(TAG, "Requesting the permissions to use NearbyApi.");
            mNearbyConnection.requestPermissions();
        }
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
        song.title = "Song";

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

        song.title = filename;

        JSONObject jsonObject = song.getJSONObject();

        try {
            File dir = getFilesDir();
            File file = new File(dir + "/localsongs/", filename);
            file.getParentFile().mkdirs();

            file.createNewFile();
            FileOutputStream outputFile = new FileOutputStream(file);
            outputFile.write(jsonObject.toString().getBytes());
            outputFile.flush();
            outputFile.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onBackPressed() {
//        //TODO: for now save the song to cloud, later show an alert dialog asking if the user wants to quit without saving
//
//        saveSongLocally("Test");
//        UploadTask uploadTask = saveSongToCloud("Test");
//
//        // Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//                exception.printStackTrace();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//            }
//        });
//    }

    @Override
    public long playButtonPressed(SoundSampleInstance soundSampleInstance) {
        if(song == null) {
            setupNewSong();
        }

        // Send soundSample info (name for now) to connected devices.
        mNearbyConnection.sendData(soundSampleInstance.getSoundSample().getName());

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
    public void addSamplePressed() {
        showAddSoundSampleDialog();
    }

    @Override
    public void startPlaying(SoundSampleInstance soundSampleInstance, long startSection) {
        synchronized (activeSampleUsages) {
            SampleUsage sampleUsage = new SampleUsage(soundSampleIndexMapping.get(soundSampleInstance), startSection);

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

        sampleUsage.loopTimes = numTimesPlayed - 1;

        song.addSampleUsage(sampleUsage);
    }

    @Override
    public void onLoaded(SoundSampleInstance soundSampleInstance) {
        //Do nothing
    }

    private void setupDrawerView(NavigationView navigationView) {
        // Disable tint on the items icon.
        navigationView.setItemIconTintList(null);

        // Set item selected listener.
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return false;
                    }
                }
        );
    }

    private String getTestSongTitle() {
        String title = song == null ? "Song" : song.title;

        return title + " " + new SimpleDateFormat("EEE, MMM d, yyyy hh:mm aaa").format(new Date());
    }

    private interface SaveSongDialogListener {
        void onTitleSet(String title);
    }

    private void saveSongDialog(String initialString, final SaveSongDialogListener titleSetHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.add_new_title));

        // Container for the input, in having this, we can add margins to the editText.
        LinearLayout parentInput = new LinearLayout(this);
        parentInput.setOrientation(LinearLayout.VERTICAL);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(initialString);
        input.setSelection(input.getText().length());

        // Get the parameters of the linearLayout and set its margins.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(PixelUtils.getPixelValue(this, getResources().getInteger(R.integer.alert_dialog_margin_left)),
                            PixelUtils.getPixelValue(this, getResources().getInteger(R.integer.alert_dialog_margin_top)),
                            PixelUtils.getPixelValue(this, getResources().getInteger(R.integer.alert_dialog_margin_right)),
                            PixelUtils.getPixelValue(this, getResources().getInteger(R.integer.alert_dialog_margin_bottom)));
        parentInput.addView(input, params);

        builder.setView(parentInput);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                titleSetHandler.onTitleSet(input.getText().toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.show();

        alertDialog.getWindow().setDimAmount(0.9f);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Make the dialog occupy 80% of the screens width.
        DisplayMetrics metrics = alertDialog.getContext().getResources()
                .getDisplayMetrics();
        int width = metrics.widthPixels;
        View view = alertDialog.getWindow().getDecorView()
                .findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = 4 * width / 5; // 80% of screen
        layoutParams.gravity = Gravity.CENTER;
        view.setLayoutParams(layoutParams);

    }

    private void showNoSongToast() {
        Toast.makeText(this, "No song data yet.  Start making a song and then save it.", Toast.LENGTH_LONG).show();
    }

    private void selectDrawerItem(MenuItem menuItem) {
        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.nav_first_element:
                if (song != null) {
                    saveSongDialog(song.title, new SaveSongDialogListener() {
                        @Override
                        public void onTitleSet(String title) {
                            saveSongToCloud(title);
                        }
                    });
                }
                else {
                    showNoSongToast();
                }

                break;
            case R.id.nav_second_element:
                if (song != null) {
                    saveSongDialog(song.title, new SaveSongDialogListener() {
                        @Override
                        public void onTitleSet(String title) {
                            saveSongLocally(title);
                        }
                    });
                }
                else {
                    showNoSongToast();
                }

                break;
            default:
                //Toast.makeText(CreateSongActivity.this, "Default case selected!", Toast.LENGTH_LONG).show();
        }

        //menuItem.setChecked(false);
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                // Un-select any selected item.
                for (int i = 0; i <nvDrawer.getMenu().size() ; i++) {
                    nvDrawer.getMenu().getItem(i).setChecked(false);
                }


                // Send a message to the other phone. For testing only!
                String data = "Sent from onDrawerClose!";
                mNearbyConnection.sendData(data);
                Toast.makeText(CreateSongActivity.this, "Message sent!", Toast.LENGTH_LONG).show();


                super.onDrawerClosed(view);
            }
        };
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void showAddSoundSampleDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddSoundSampleDialogFragment addSoundSampleDialogFragment = AddSoundSampleDialogFragment.newInstance();
        addSoundSampleDialogFragment.show(fm, "fragment_add_sound_sample");
    }

    @Override
    public void onSoundSampleAdded(String title) {
        onAddNewSample(SoundSample.SOUND_SAMPLES.get(title));
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

    private void setupNearbyConnection() {
        mNearbyConnection = new NearbyConnection(this, this);

        // Set the listener to manage communication between devices.
        mNearbyConnection.setNearbyConnectionListener(new NearbyConnection.NearbyConnectionListener() {
            @Override
            public void sendCurrentSong() {
                // A discoverer just connected to us. Send all of the song info.
                JSONObject data = new JSONObject();
                try {
                    data.put("Sample1", 1);
                    data.put("Sample2", 2);
                    data.put("Sample3", 3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send the song.
                mNearbyConnection.sendData(data.toString());
            }

            @Override
            public void receiveCurrentSong(String song) {
                //Do nothing.
            }

            @Override
            public void receiveNewSample(String sample) {
                // Do nothing, for now.
            }
        });
    }

}
