package com.codepath.collabdj.models;

import android.content.Context;

import com.codepath.collabdj.utils.SamplePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codepath.collabdj.utils.SamplePlayer.PlayInstanceState.STOPPED;

/**
 * Created by ilyaseletsky on 10/15/17.
 */

public class SoundSampleInstance implements SamplePlayer.SampleHandleListener {
    public interface Listener {
        void startPlaying(SoundSampleInstance soundSampleInstance, long startSection);

        void stopPlaying(SoundSampleInstance soundSampleInstance, int numTimesPlayed);
    }

    protected class PlayInstanceInfo {
        PlayInstanceInfo(long startSection) {
            this.startSection = startSection;

            numTimesPlayed = 0;
        }

        long startSection;
        int numTimesPlayed;
    }

    SoundSample soundSample;

    SamplePlayer.SampleHandle sampleHandle;

    List<SamplePlayer.SampleHandle.PlayInstance> queuedPlayInstances;
    Map<SamplePlayer.SampleHandle.PlayInstance, PlayInstanceInfo> playInstanceInfos;

    public Listener listener;

    public SoundSampleInstance(SoundSample soundSample, SamplePlayer samplePlayer, Context context, Listener listener) {
        this.soundSample = soundSample;

        // This checks are needed for now, to make the heterogenous recyclerview work with a dummy sample. It will go away.
        if (soundSample != null && samplePlayer != null) {
            if (soundSample.getPath() != null) {
                sampleHandle = samplePlayer.newSample(soundSample.getPath(), soundSample.getDuration());
            } else {
                sampleHandle = samplePlayer.newSample(context, soundSample.getResourceId(), soundSample.getDuration());
            }
        }

        queuedPlayInstances = new ArrayList<>();
        playInstanceInfos = new HashMap<>();

        this.listener = listener;
    }

    public SoundSample getSoundSample() {
        return this.soundSample;
    }

    public SamplePlayer.PlayInstanceState getCurrentPlayState() {
        SamplePlayer.SampleHandle.PlayInstance currentPlayInstance = getCurrentPlayInstance();

        if(currentPlayInstance == null) {
            return STOPPED;
        }
        else {
            return currentPlayInstance.getPlayState();
        }
    }

    public SamplePlayer.SampleHandle.PlayInstance getCurrentPlayInstance() {
        synchronized (queuedPlayInstances) {
            if (queuedPlayInstances.isEmpty()) {
                return null;
            }

            for (SamplePlayer.SampleHandle.PlayInstance playInstance : queuedPlayInstances) {
                if (playInstance.getPlayState() != STOPPED) {
                    return playInstance;
                }
            }

            return null;
        }
    }

    public void queueSample(long section, long millisecondsPerSection, long songStartTime, int loopAmount) {
        SamplePlayer.SampleHandle.PlayInstance playInstance = sampleHandle.queueSample(songStartTime + (section * millisecondsPerSection), loopAmount);

        synchronized (queuedPlayInstances) {
            queuedPlayInstances.add(playInstance);
            playInstanceInfos.put(playInstance, new PlayInstanceInfo(section));
        }
    }

    public void stop() {
        SamplePlayer.SampleHandle.PlayInstance currentPlayInstance = getCurrentPlayInstance();

        if (currentPlayInstance != null) {
            currentPlayInstance.stop();
        }
    }

    public boolean isLoaded() {
        return sampleHandle.isLoaded();
    }

    @Override
    public void onPlayOnce(SamplePlayer.SampleHandle.PlayInstance playInstance) {
        //Synchronizing around queuedPlayInstances even though we're accessing playInstanceInfos since that's how it is everywhere else.
        //They're closely tied together as one.  Could even wrap them in an inner class and syncrhonize around that if I really wanted to.
        synchronized (queuedPlayInstances) {
            PlayInstanceInfo playInstanceInfo = playInstanceInfos.get(playInstance);
            assert(playInstanceInfo != null);

            if (playInstanceInfo.numTimesPlayed == 0 && listener != null) {
                listener.startPlaying(this, playInstanceInfo.startSection);
            }

            playInstanceInfo.numTimesPlayed += 1;
        }
    }

    @Override
    public void onStop(SamplePlayer.SampleHandle.PlayInstance playInstance) {
        synchronized (queuedPlayInstances) {
            PlayInstanceInfo playInstanceInfo = playInstanceInfos.get(playInstance);
            assert(playInstanceInfo != null);

            if (playInstanceInfo.numTimesPlayed > 0 && listener != null) {
                listener.stopPlaying(this, playInstanceInfo.numTimesPlayed);
            }

            queuedPlayInstances.remove(playInstance);
            playInstanceInfos.remove(playInstance);
        }
    }

    @Override
    public void onLoaded(SamplePlayer.SampleHandle sampleHandle) {

    }
}
