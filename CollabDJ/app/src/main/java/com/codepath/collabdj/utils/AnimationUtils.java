package com.codepath.collabdj.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.content.ContextCompat;
import android.util.Property;
import android.view.View;

import com.codepath.collabdj.R;
import com.codepath.collabdj.activities.CreateSongActivity;

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

    public static void triggerActionReceivedAnimation(final View view) {
        // This is horrible, but it was the easiest way I could come up with.
        CreateSongActivity createSongActivity = (CreateSongActivity) view.getContext();
        if (createSongActivity.fabEndpoint.getVisibility() == View.VISIBLE) {
            int colorFrom = ContextCompat.getColor(view.getContext(), R.color.transparent);
            int colorTo;
            if (createSongActivity.isHost) {
                colorTo = ContextCompat.getColor(view.getContext(), R.color.endpointColor1);
            } else {
                colorTo = ContextCompat.getColor(view.getContext(), R.color.endpointColor2);
            }
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo, colorFrom);
            colorAnimation.setDuration(750); // milliseconds
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    view.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                }
            });
            colorAnimation.start();
        }
    }

}
