package com.codepath.collabdj.activities.utils;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by tiago on 10/15/17.
 */

public class PlayPauseButton extends android.support.v7.widget.AppCompatImageButton {

    private boolean isPlaying;

    public PlayPauseButton(Context context) {
        super(context);
        isPlaying = false;
    }

    public PlayPauseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        isPlaying = false;
    }

    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isPlaying = false;
    }

    public void setIsPlaying(boolean value) {
        this.isPlaying = value;
        this.setImageAccordingToState();
    }

    // Toggles the value of `isPlaying` flag and updates the button image accordingly.
    public void toggleIsPlaying() {

        // Toggle `isPlaying`.
        this.isPlaying = !this.isPlaying;

        // Change the `play/pause` image according to the new state of the button.
        if (this.isPlaying) {
            this.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            this.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    public void setImageAccordingToState() {
        if (this.isPlaying) {
            this.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            this.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }
}
