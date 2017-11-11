package com.codepath.collabdj.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.codepath.collabdj.R;

/**
 * Created by tiago on 11/10/17.
 */

public class StopRestartButton extends AppCompatImageButton {

    public static final int STOP = 0;
    public static final int RESTART = 1;

    int state;

    public StopRestartButton(Context context) {
        super(context);
    }

    public StopRestartButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StopRestartButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setToStop() {

        if (state == RESTART) {
            state = STOP;
        } else {
            return;
        }

        this.setImageResource(R.drawable.ic_stop_white_48dp);
    }

    public void setToRestart() {

        if (state == STOP) {
            state = RESTART;
        } else {
            return;
        }

        this.setImageResource(R.drawable.ic_autorenew_white_48dp);
    }

    public int getState() {
        return state;
    }

}