package com.codepath.collabdj.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.codepath.collabdj.R;
import com.codepath.collabdj.models.SampleUsage;
import com.codepath.collabdj.models.Song;
import com.codepath.collabdj.models.SoundSampleInstance;
import com.codepath.collabdj.utils.OnSwipeTouchListener;
import com.codepath.collabdj.utils.SamplePlayer;
import com.codepath.collabdj.views.SongProgressView;
import com.codepath.collabdj.views.StopRestartButton;

import java.util.ArrayList;
import java.util.List;

public class PlaySongActivity extends AppCompatActivity implements SoundSampleInstance.Listener {
    public static final String SONG_KEY = "song";

    Song song;
    SamplePlayer samplePlayer;

    List<SoundSampleInstance> soundSampleInstances;
    long songStartTime;

    int numLoadingSamples;

    SongProgressView songPositionBar;

    ImageView ivGif;
    TextView tvSongTitle;
    TextSwitcher tsPlayerStatus;
    StopRestartButton ibStopRestart;

    public static void launch(Song song, Context context, View view) {
        Intent i = new Intent(context, PlaySongActivity.class);
        i.putExtra(PlaySongActivity.SONG_KEY, song);

        View tvSongTitle = view.findViewById(R.id.sharedSongTV);
        View ivQuaver = view.findViewById(R.id.ivQuaver);

        // Setup shared elements activity transition.
        Pair<View, String> p1 = Pair.create((View)tvSongTitle, "songTitle");
        Pair<View, String> p2 = Pair.create((View)ivQuaver, "quaverTransition");

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, p1, p2);
        context.startActivity(i, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        song = (Song) getIntent().getSerializableExtra(SONG_KEY);

        // Setup the text switcher.
        setupPlayerStatusTextSwitcher();

        samplePlayer = new SamplePlayer(64);

        songPositionBar = (SongProgressView) findViewById(R.id.songPositionBar);

        tvSongTitle = (TextView) findViewById(R.id.tvSongTitle);
        tvSongTitle.setText(song.getTitle());

        // Find the ImageView to display the GIF
        ivGif = (ImageView) findViewById(R.id.ivGif);
        // Display the GIF (from raw resource) into the ImageView.
        // ImageView is INVISIBLE now and will become VISIBLE when the song loads.
        Glide.with(this).load(getResIdForPlayerBackground()).asGif().dontAnimate()
                .into(ivGif);

        //Add the samples
        soundSampleInstances = new ArrayList<>(song.getNumSoundSamples());
        numLoadingSamples = 0;

        for(int i = 0; i < song.getNumSoundSamples(); ++i) {
            SoundSampleInstance soundSampleInstance = new SoundSampleInstance(song.getSoundSample(i), samplePlayer, this, this);

            if(!soundSampleInstance.isLoaded()) {
                numLoadingSamples++;
            }

            soundSampleInstances.add(soundSampleInstance);
        }

        // Setup stop/restart button.
        setupStopRestartButton();

        // Set onSwipe listeners.
        setupSwipeListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        samplePlayer.kill();
    }

    public void startSong() {
        //Offset by a little to start the song sooner
        final long startOffset = (long)((float) song.getNumMillisecondsPerSection() * .9f);

        songStartTime = SamplePlayer.getCurrentTimestamp() - startOffset;

        long endTime = 0;

        //Queue up the samples so the song starts playing
        for(int i = 0; i < song.getNumSampleUsages(); ++i) {

            SampleUsage sampleUsage = song.getSampleUsage(i);

            SoundSampleInstance soundSampleInstance = soundSampleInstances.get(sampleUsage.getSoundSampleIndex());

            long sampleEndTime = song.getSectionTimestampFromStart(sampleUsage.getStartSection())
                    + (sampleUsage.loopTimes * soundSampleInstance.getSoundSample().getDuration()) ;

            soundSampleInstance.queueSample(sampleUsage.getStartSection(),
                    song.getNumMillisecondsPerSection(),
                    songStartTime,
                    sampleUsage.loopTimes);

            if (sampleEndTime > endTime) {
                endTime = sampleEndTime;
            }

        }

        // Make the background GIF visible now that the song loaded.
        ivGif.setVisibility(View.VISIBLE);
        // Make the stop/restart button visible now that the song loaded.
        ibStopRestart.setVisibility(View.VISIBLE);

        // Change the status textView to "Playing".
        tsPlayerStatus.setText(getString(R.string.player_status_playing));


        songPositionBar.setVisibility(View.VISIBLE);
        songPositionBar.setMax((int)endTime);
        songPositionBar.setProgress(0);

        songPositionBar.listener = new SongProgressView.UpdateListener() {
            @Override
            public void updateProgressView(SongProgressView progressView) {
                progressView.setProgress((int) ((SamplePlayer.getCurrentTimestamp() - (songStartTime + startOffset))));
            }
        };

        songPositionBar.invalidate();

    }

    @Override
    public void startPlaying(SoundSampleInstance soundSampleInstance, long startSection) {

    }

    @Override
    public void stopPlaying(SoundSampleInstance soundSampleInstance, int numTimesPlayed) {

    }

    @Override
    public void onLoaded(SoundSampleInstance soundSampleInstance) {
        --numLoadingSamples;

        if (numLoadingSamples == 0) {
            View loadingBar = findViewById(R.id.loadingBar);
            loadingBar.setVisibility(View.INVISIBLE);

            startSong();
        }
    }

    /*
     * Attaches the swipe events to the background image GIF.
     */
    private void setupSwipeListeners() {
        ivGif.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                // End the activity.
                ActivityCompat.finishAfterTransition(PlaySongActivity.this);
            }

            @Override
            public void onSwipeLeft() {
                // End the activity.
                ActivityCompat.finishAfterTransition(PlaySongActivity.this);
            }

            @Override
            public void onSwipeUp() {
                // End the activity.
                ActivityCompat.finishAfterTransition(PlaySongActivity.this);
            }

            @Override
            public void onSwipeRight() {
                // End the activity.
                ActivityCompat.finishAfterTransition(PlaySongActivity.this);
            }
        });
    }

    private void setupPlayerStatusTextSwitcher() {
        tsPlayerStatus = (TextSwitcher) findViewById(R.id.tsPlayerStatus);
        tsPlayerStatus.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tvStatusText = new TextView(PlaySongActivity.this);
                tvStatusText.setText(getString(R.string.player_status_loading));
                tvStatusText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tvStatusText.setTypeface(tvStatusText.getTypeface(), Typeface.BOLD);
                return tvStatusText;
            }
        });
        tsPlayerStatus.setInAnimation(this, android.R.anim.fade_in);
        tsPlayerStatus.setOutAnimation(this, android.R.anim.fade_out);
    }

    private void setupStopRestartButton() {
        ibStopRestart = (StopRestartButton) findViewById(R.id.ibStopRestart);

        // Set To Stop State
        ibStopRestart.setToStop();

        ibStopRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch state.
                if (ibStopRestart.getState() == StopRestartButton.RESTART) {
                    ibStopRestart.setToStop();
                    // This does nothing in terms of stopping the song!
                } else {
                    ibStopRestart.setToRestart();
                    // This does nothing in terms of restarting the song!
                }
            }
        });

    }

    /*
     * Returns the Resource Identifier of a randomly selected background file for the player.
     */
    private int getResIdForPlayerBackground() {
       /*
        Random r = new Random();
        int randomInt = r.nextInt(7) + 1;
        int res = 1;

        switch (randomInt) {
            case 1:
                res = R.raw.pbg_1;
                break;
            case 2:
                res = R.raw.pbg_2;
                break;
            case 3:
                res = R.raw.pbg_3;
                break;
            case 4:
                res = R.raw.pbg_4;
                break;
            case 5:
                res = R.raw.pbg_5;
                break;
            case 6:
                res = R.raw.pbg_6;
                break;
            case 7:
                res = R.raw.pbg_7;
                break;
            default:
                res = 1;
                break;
        }

        return res;
        */
       return R.raw.pbg_1;
    }
}
