package com.codepath.collabdj.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by tiago on 11/8/17.
 */

public class PixelUtils {

    public static int getPixelValue(Context context, int dp) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, resources.getDisplayMetrics());
    }

}
