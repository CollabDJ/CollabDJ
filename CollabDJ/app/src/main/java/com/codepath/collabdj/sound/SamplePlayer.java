package com.codepath.collabdj.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by ilyaseletsky on 10/13/17.
 */

public class SamplePlayer {
    static String TAG = "SamplePlayer";

    /**
     * Pass these to Sample player to queue up a sample at some point in time.
     */
    public class SampleHandle {
        protected class PlayInstance {
            /**
             * How many more times should the sample loop.
             * This counts down every time the sample plays.
             * If it's <0 then it loops infinitely until stopped.
             */
            protected int loopAmount;

            /**
             * Whether or not the sample should be playing.
             * It may not have started truly playing through speakers due to not being loaded yet.
             * Upon loading, if this is true, it'll start.
             * This gets set back to false after the sample is finally stopped on its last loop.
             * This is also false if the sample is queued but isn't supposed to have started playing yet.
             */
            protected boolean shouldBePlaying;

            /**
             * Task that is queued to run to begin playing the sample.
             * This is set up either to play the sample for the first time in the future or to run at
             * the end of a current loop iteration of the sample.
             * This should simply call tryBeginPlaying()
             */
            protected ScheduledFuture<?> sampleTask;

            /**
             * Creates a new play instance.
             * You should immediately call startSchedule afterwards.
             * Can't do it in the constructor itself.
             *
             * @param loopAmount
             * @param delayBeforePlaying Duration in milliseconds to delay before actually playing the sample
             *                           Pass in <= 0 if it should start playing immediately
             */
            protected PlayInstance(int loopAmount, long delayBeforePlaying) {
                Log.v(TAG, "PlayInstance initialized for sample " + sampleId + " with loopAmount " + loopAmount + " and delay " + delayBeforePlaying  + " and period " + duration);

                //set loop amount to + 1 since tryBeginPlaying actually needs the value +1, but < 0 means loop infinitely
                if (loopAmount >= 0) {
                    loopAmount += 1;
                }

                this.loopAmount = loopAmount;

                shouldBePlaying = false;

                if (delayBeforePlaying < 0) {
                    delayBeforePlaying = 0;
                }

                //set up the task
                sampleTask = timedTaskRunner.scheduleAtFixedRate(new Runnable() {
                                                                     @Override
                                                                     public void run() {
                                                                         tryBeginPlaying();
                                                                     }
                                                                 },
                        delayBeforePlaying, duration, TimeUnit.MILLISECONDS);
            }

            /**
             * The actual work that runs inside sampleTask.
             * When running at the end of the sample this is responsible for either starting to loop
             * the sample again or for setting up the sample for being stopped.
             */
            protected void tryBeginPlaying() {
                Log.v(TAG, "PlayInstance tryBeginPlaying() for sample " + sampleId + " has " + loopAmount + " loops left. (-1 means infinite) Timestamp " + getCurrentTimestamp());

                //no more loops to play
                if (loopAmount == 0) {
                    stop();
                    return;
                }

                //decrement remaining loops if needed
                if (loopAmount > 0) {
                    loopAmount -= 1;
                }

                shouldBePlaying = true;

                playOnce();
            }

            /**
             * Stops the sample if it's currently queued up.
             * Sets everything up to be stopped.
             * Removes this from the SampleHandle's list of queued samples.
             */
            protected void stop() {
                Log.v(TAG, "PlayInstance stop() for sample " + sampleId);

                sampleTask.cancel(true);

                shouldBePlaying = false;
                loopAmount = 0;

                playInstances.remove(this);
            }
        }

        /**
         * Scheduled instances of playing the sound sample.
         */
        protected Set<PlayInstance> playInstances;

        /**
         * Corresponds to the sampleId in the sound pool
         */
        protected int sampleId;

        /**
         * Whether or not the sample is fully loaded and ready to play
         */
        protected boolean isLoaded;

        /**
         * Duration is configured from the outside to help loop the sample.
         * We don't actually play the sound looped using SoundPool but manually loop the sound.
         * This way the sample can have some parts of the sound after the end of the sound data for when it fades out to cleanly stop.
         * This way instruments don't just instantly cut off at the end of a sample if there is some extra sound at the end, like a drum crash.
         */
        protected long duration;

        protected SampleHandle(int sampleId, boolean isLoaded, long duration) {
            this.isLoaded = isLoaded;
            this.sampleId = sampleId;
            this.duration = duration;

            playInstances = new HashSet<>();
        }

        public SamplePlayer getParentSamplePlayer() {
            return SamplePlayer.this;
        }

        /**
         * Call this to start playing a sample at a specific timestamp in the future.
         * @param timestamp
         * @param loopAmount Number of times to loop.  In song playback mode this will be the
         *                   number of times the sample was set to loop when the song was created.
         *                   In song creation mode, this will most likely be either 0 to play once
         *                   or -1 to loop infinitely until queueStop() is called.
         */
        public void queueSample(long timestamp, int loopAmount) {
            Log.v(TAG, "SampleHandle queueSample() sampleId " + sampleId + " with timestamp " + timestamp + " and loop amount " + loopAmount);

            playInstances.add(new PlayInstance(loopAmount, timestamp - getCurrentTimestamp()));
        }

        /**
         * This will tell the sample to stop playing at the end of its next loop if it's currently playing.
         * It will also abort all instances of the sample playing.
         * It's assumed this will only be called in song creation mode and never in song playback mode since
         * it wipes all queued play instances.
         */
        public void stop() {
            Log.v(TAG, "SampleHandle stop() sampleId " + sampleId);

            //Create a copy since playInstance.stop() also handles removing itself from the playInstances set
            //Can't iterate and remove at the same time
            //It's expected that this should usually have only 1 item or so in it anyway so creating a copy isn't a big deal
            Set<PlayInstance> playInstancesCopy = new HashSet<>(playInstances);

            for(PlayInstance playInstance : playInstancesCopy) {
                playInstance.stop();
            }
        }

        protected void playOnce() {
            Log.v(TAG, "SampleHandle playOnce() sampleId " + sampleId);

            if (isLoaded) {
                Log.v(TAG, "SampleHandle IS loaded and really playing sampleId " + sampleId);
                soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
            else {
                Log.v(TAG, "SampleHandle NOT loaded yet sampleId " + sampleId);
            }
        }

        protected void onSampleLoaded() {
            Log.v(TAG, "SampleHandle onSampleLoaded() sampleId " + sampleId);

            isLoaded = true;

            for(PlayInstance playInstance : playInstances) {
                if(playInstance.shouldBePlaying) {
                    Log.v(TAG, "SampleHandle onSampleLoaded() and should be playing sampleId " + sampleId);

                    //if there was a way to play the sample at a specific point in time instead of from the start, do that
                    //In the future, SoundPool can be replaced with OpenAL or OpenSL for greater control
                    //I expect most sounds to load almost instantly so it should be fine
                    playOnce();
                }
            }
        }
    }

    SoundPool soundPool;

    ScheduledExecutorService timedTaskRunner;

    /**
     * Lookup of sampleId to SampleHandles
     */
    Map<Integer, List<SampleHandle>> sampleHandles;

    /**
     * Lookup of sound path to sampleId in case the same sound is loaded multiple times
     */
    Map<String, Integer> loadedSounds;

    /**
     * Lookup of sound resource to sampleId in case the same sound is loaded multiple times
     */
    Map<Integer, Integer> loadedSoundResources;

    public SamplePlayer(int maxStreams) {
        //To support API 16, just use the deprecated version
        //SoundPool.Builder builder = new SoundPool.Builder();
        soundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    SamplePlayer.this.onLoadComplete(sampleId);
                }
            }
        });

        timedTaskRunner = Executors.newScheduledThreadPool(10);

        sampleHandles = new HashMap<>();
        loadedSounds = new HashMap<>();
        loadedSoundResources = new HashMap<>();
    }

    protected void onLoadComplete(int sampleId) {
        List<SampleHandle> sampleHandlesList = sampleHandles.get(sampleId);

        if (sampleHandlesList == null) {
            Log.e(TAG, "onLoadComplete() called for sample handle id: " + sampleId + " but didn't find any samples.");
        }
        else {
            for(SampleHandle sampleHandle : sampleHandlesList) {
                sampleHandle.onSampleLoaded();
            }
        }
    }

    /**
     * Creates a new sample given a path to a file.
     * If the file has already been loaded before it reuses an existing loaded sound sample instead of loading new data into memory.
     * @param path Path to the sound data
     * @param duration Duration is configured from the outside to help loop the sample.
     *      We don't actually play the sound looped using SoundPool but manually loop the sound.
     *      This way the sample can have some parts of the sound after the end of the sound data for when it fades out to cleanly stop.
     *      This way instruments don't just instantly cut off at the end of a sample, like a drum.
     * @return A unique sound handle that can be used for playing or stopping a sound
     */
    public SampleHandle newSample(String path, long duration) {
        Integer sampleId = loadedSounds.get(path);

        boolean isLoaded = sampleId != null;

        if (!isLoaded) {
            sampleId = soundPool.load(path, 1);
        }

        return newHandle(sampleId, isLoaded, duration);
    }

    /**
     * Creates a new sample given a resource id.
     * If the file has already been loaded before it reuses an existing loaded sound sample instead of loading new data into memory.
     * @param context the application context
     * @param resId Resource id of sound data
     * @param duration Duration is configured from the outside to help loop the sample.
     *      We don't actually play the sound looped using SoundPool but manually loop the sound.
     *      This way the sample can have some parts of the sound after the end of the sound data for when it fades out to cleanly stop.
     *      This way instruments don't just instantly cut off at the end of a sample, like a drum.
     * @return A unique sound handle that can be used for playing or stopping a sound
     */
    public SampleHandle newSample(Context context, int resId, long duration) {
        Integer sampleId = loadedSoundResources.get(resId);

        boolean isLoaded = sampleId != null;

        if (!isLoaded) {
            sampleId = soundPool.load(context, resId, 1);
        }

        return newHandle(sampleId, isLoaded, duration);
    }

    protected SampleHandle newHandle(int sampleId, boolean isLoaded, long duration) {
        SampleHandle sampleHandle = new SampleHandle(sampleId, isLoaded, duration);

        List<SampleHandle> sampleHandlesList = sampleHandles.get(sampleId);

        if (sampleHandlesList == null) {
            sampleHandlesList = new ArrayList<>(1);
            sampleHandles.put(sampleId, sampleHandlesList);
        }

        sampleHandlesList.add(sampleHandle);

        return sampleHandle;
    }

    public static long getCurrentTimestamp() {
        return new Date().getTime();
    }
}
