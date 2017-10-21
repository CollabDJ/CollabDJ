package com.codepath.collabdj.models;

import android.content.Context;

import com.codepath.collabdj.adapters.SoundSamplesAdapter;
import com.codepath.collabdj.utils.SamplePlayer;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.collabdj.utils.SamplePlayer.PlayInstanceState.STOPPED;

/**
 * Created by ilyaseletsky on 10/15/17.
 */

public class SoundSampleInstance implements SoundSamplesAdapter.SoundSamplePlayListener {
    SoundSample soundSample;

    SamplePlayer.SampleHandle sampleHandle;

    List<SamplePlayer.SampleHandle.PlayInstance> queuedPlayInstances;

    public SoundSampleInstance(SoundSample soundSample, SamplePlayer samplePlayer, Context context) {
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

    public void queueSample(long timestamp, int loopAmount) {
        SamplePlayer.SampleHandle.PlayInstance playInstance = sampleHandle.queueSample(timestamp, loopAmount);

        synchronized (queuedPlayInstances) {
            queuedPlayInstances.add(playInstance);
        }
    }

    public void stop() {
        SamplePlayer.SampleHandle.PlayInstance currentPlayInstance = getCurrentPlayInstance();

        if (currentPlayInstance != null) {
            currentPlayInstance.stop();
        }
    }

    @Override
    public void playButtonPressed(SoundSampleInstance soundSampleInstance) {
        synchronized (queuedPlayInstances) {
            queuedPlayInstances.remove(soundSampleInstance);
        }
    }
}
