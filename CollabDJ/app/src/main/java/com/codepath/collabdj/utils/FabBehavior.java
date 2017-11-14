package com.codepath.collabdj.utils;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.codepath.collabdj.activities.CreateSongActivity;

/**
 * Created by tiago on 11/14/17.
 */

public class FabBehavior extends FloatingActionButton.Behavior {


//    private static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutLinearInInterpolator();
    private static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();

    private int mOffscreenTranslation;
    private int mRollingState = RollingFabState.IDLE;
    private float mTensionFactor = 0f;
    private Context mContext;

    private ViewPropertyAnimatorListener mRollingInListener = new ViewPropertyAnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(View view) {
            setRollingState(RollingFabState.ROLLING_IN);
        }

        @Override
        public void onAnimationEnd(View view) {
            mTensionFactor = 0.0f;
            setRollingState(RollingFabState.IDLE);
            // Show the user connected snackbar.
            CreateSongActivity createSongActivity = (CreateSongActivity) mContext;
            createSongActivity.showEndpointConnectedSnackbar();
        }
    };

    private ViewPropertyAnimatorListener mRollingOutListener = new ViewPropertyAnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(View view) {
            setRollingState(RollingFabState.ROLLING_OUT);
        }

        @Override
        public void onAnimationEnd(View view) {
            setRollingState(RollingFabState.ROLLED_OUT);
        }
    };

    @IntDef({RollingFabState.IDLE, RollingFabState.ROLLING_OUT, RollingFabState.ROLLING_IN, RollingFabState.ROLLED_OUT})
    private @interface RollingFabState {
        int ROLLING_OUT = -1;
        int ROLLING_IN = 0;
        int ROLLED_OUT = 1;
        int IDLE = 2;
    }

    public FabBehavior(Context context) {
        super();
        this.mContext = context;
    }

    public FabBehavior(Context context, AttributeSet attrs) {
        super();
    }

    // Makes the FAB show/hide on recyclerview scroll.
    @Override
    @SuppressWarnings("deprecation")
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        nestedScrollAxes);
    }

    // Makes the FAB show/hide on recyclerview scroll.
    @Override
    @SuppressWarnings("deprecation")
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            // User scrolled down and the FAB is currently visible -> hide the FAB
            child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    fab.setVisibility(View.INVISIBLE);
                }
            });
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            // User scrolled up and the FAB is currently not visible -> show the FAB
            child.show();
        }
    }

    // Makes the FAB roll into screen, used when a user connects to us.
    public void rollInFab(FloatingActionButton fab) {
        //fab.setVisibility(View.VISIBLE);
        rollInFabCompletely(fab);
    }

    // Makes the FAB roll out of screen, used when a user disconects from us.
    public void rollOutFab(FloatingActionButton fab) {
        rollOutFabCompletely(fab);
    }

    private void rollInFabCompletely(FloatingActionButton child) {
        ViewCompat.animate(child).translationX(0f).translationY(0f).rotation(0).setDuration(200)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(mRollingInListener).start();

    }

    private void rollOutFabCompletely(FloatingActionButton child) {
        mOffscreenTranslation = 250;
        ViewCompat.animate(child).translationX(mOffscreenTranslation).translationY(mOffscreenTranslation).rotation(360).setDuration(200).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(mRollingOutListener).start();
    }

    private void setRollingState(@RollingFabState int rollingState) {
        this.mRollingState = rollingState;
    }

}
