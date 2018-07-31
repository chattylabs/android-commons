package com.chattylabs.sdk.android.common;

import android.content.Context;
import android.util.TypedValue;

public class DimensionUtils {

    /**
     * @param context Context
     * @param unit TypedValue
     * @param value int
     * @return int
     */
    public static int getDimension(Context context, int unit, int value) {
        return (int) getDimension(context, unit, Integer.valueOf(value).floatValue());
    }

    /**
     * @param context Context
     * @param unit TypedValue
     * @param value float
     * @return float
     */
    public static float getDimension(Context context, int unit, float value) {
        return TypedValue.applyDimension(unit, value, context.getResources().getDisplayMetrics());
    }
}
