package com.codepath.collabdj.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ilyaseletsky on 10/13/17.
 */

public class SamplePlayer {
    static String TAG = "SamplePlayer";

    /**
     * Pass these to Sample player to queue up a sample at some point in time.
     */
    public class SampleHandle {
        /**
         * Timestamp that this sample has been queued up to play.
         * If the sample finishes loading after queuedStartTime it'll start playing it at the correct time.
         */
        protected long queuedStartTime;

        /**
         * Timestamp that this sample has been queued up to stop playing.
         */
        protected long queuedStopTime;

        /**
         * Corresponds to the sampleId in the sound pool
         */
        protected int sampleId;

        /**
         * Corresponds to the streamId in the sound pool if it's currently playing
         */
        protected int streamId;

        /**
         * Whether or not the sample is fully loaded and ready to play
         */
        protected boolean isLoaded;

        protected SampleHandle(int sampleId, boolean isLoaded) {
            this.isLoaded = isLoaded;
            this.sampleId = sampleId;

            queuedStartTime = -1;
            queuedStopTime = -1;
            streamId = 0;
        }

        public SamplePlayer getParentSamplePlayer() {
            return SamplePlayer.this;
        }
    }

    SoundPool soundPool;

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
                sampleHandle.isLoaded = true;

                //begin playing the sample at the right time if it has already been queued to play and isn't supposed to be stopped
            }
        }
    }

    /**
     * Creates a new sample given a path to a file.
     * If the file has already been loaded before it reuses an existing loaded sound sample instead of loading new data into memory.
     * @param path Path to the sound data
     * @return A unique sound handle that can be used for playing or stopping a sound
     */
    public SampleHandle newSample(String path) {
        Integer sampleId = loadedSounds.get(path);

        boolean isLoaded = sampleId != null;

        if (!isLoaded) {
            sampleId = soundPool.load(path, 1);
        }

        return newHandle(sampleId, isLoaded);
    }

    /**
     * Creates a new sample given a resource id.
     * If the file has already been loaded before it reuses an existing loaded sound sample instead of loading new data into memory.
     * @param context the application context
     * @param resId Resource id of sound data
     * @return A unique sound handle that can be used for playing or stopping a sound
     */
    public SampleHandle newSample(Context context, int resId) {
        Integer sampleId = loadedSoundResources.get(resId);

        boolean isLoaded = sampleId != null;

        if (!isLoaded) {
            sampleId = soundPool.load(context, resId, 1);
        }

        return newHandle(sampleId, isLoaded);
    }

    protected SampleHandle newHandle(int sampleId, boolean isLoaded) {
        SampleHandle sampleHandle = new SampleHandle(sampleId, isLoaded);

        List<SampleHandle> sampleHandlesList = sampleHandles.get(sampleId);

        if (sampleHandlesList == null) {
            sampleHandlesList = new ArrayList<>(1);
            sampleHandles.put(sampleId, sampleHandlesList);
        }

        sampleHandlesList.add(sampleHandle);

        return sampleHandle;
    }

    /**
     * Call this to start playing a sample at a specific timestamp in the future
     * @param sampleHandle Sound sample to play
     * @param timeStamp
     * @param loopAmount
     */
    public void queueSample(SampleHandle sampleHandle, long timeStamp, int loopAmount) {
        if (sampleHandle.getParentSamplePlayer() != this) {
            Log.e(TAG, "queueSample() called for sample handle that is from a different instance of SamplePlayer.");
            return;
        }

        final SampleHandle playHandle = sampleHandle;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO: for now just play the sample
                if (playHandle.isLoaded) {
                    playHandle.streamId = soundPool.play(playHandle.sampleId, 1.0f, 1.0f, 0, -1, 1.0f);
                }
            }
        }, 1000);


    }

    /**
     * Call this to stop playing a sample at a specific timestamp in the future
     * @param sampleHandle
     * @param timeStamp
     */
    public void stopSample(SampleHandle sampleHandle, long timeStamp) {

    }
}
