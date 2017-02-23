package com.ckt.ckttodo.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by ckt on 2/23/17.
 */

public class ScreenUtils {

    public static int dp2px(Context context, int dp){
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

}
