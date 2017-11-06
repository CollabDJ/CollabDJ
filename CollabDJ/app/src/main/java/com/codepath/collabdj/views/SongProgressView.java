package com.codepath.collabdj.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by ilyaseletsky on 11/5/17.
 */

public class SongProgressView extends ProgressBar {
    public interface UpdateListener {
        void updateProgressView(SongProgressView progressView);
    }

    public UpdateListener listener;

    public SongProgressView(Context context) {
        super(context);
        configureProgressView();
    }

    public SongProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        configureProgressView();
    }

    public SongProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configureProgressView();
    }

    protected void configureProgressView() {
        
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        if (listener != null) {
            listener.updateProgressView(this);
        }

        super.onDraw(canvas);

        if (listener != null) {
            invalidate();
        }
    }
}
