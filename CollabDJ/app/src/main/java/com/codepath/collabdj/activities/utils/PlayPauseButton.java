package com.codepath.collabdj.activities.utils;

import android.content.Context;
import android.util.AttributeSet;

import com.codepath.collabdj.activities.models.SoundSampleInstance;

/**
 * Created by tiago on 10/15/17.
 */

public class PlayPauseButton extends android.support.v7.widget.AppCompatImageButton {
    public PlayPauseButton(Context context) {
        super(context);
    }

    public PlayPauseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPlayState(SoundSampleInstance.PlayState playState) {
        switch(playState) {
            case STOP_QUEUED:
            case STOPPED:
                setImageResource(android.R.drawable.ic_media_play);
                break;
            default:
                setImageResource(android.R.drawable.ic_media_pause);
                break;
        }
    }
}
