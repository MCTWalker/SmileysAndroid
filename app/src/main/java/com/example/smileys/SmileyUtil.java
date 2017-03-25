package com.example.smileys;

import android.content.Context;

/**
 * Created by Maura on 3/25/2017.
 */

public final class SmileyUtil {

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
