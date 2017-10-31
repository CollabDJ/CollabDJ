package com.codepath.collabdj.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Property;
import android.view.View;

/**
 * Created by ilyaseletsky on 10/30/17.
 */

public class AnimationUtils {
    public static void setupPulsatingAnimation(View view,
                                               long duration,
                                               int repeatCount,
                                               float start,
                                               float end)
    {
        setupPulsatingAnimation(view, View.SCALE_X, duration, repeatCount, start, end);
        setupPulsatingAnimation(view, View.SCALE_Y, duration, repeatCount, start, end);
    }

    private static void setupPulsatingAnimation(View view,
                                                Property property,
                                                long duration,
                                                int repeatCount,
                                                float start,
                                                float end)
    {
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(view, property, start, end);
        scaleAnim.setDuration(duration);
        scaleAnim.setRepeatCount(repeatCount);
        scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnim.start();
    }
}
