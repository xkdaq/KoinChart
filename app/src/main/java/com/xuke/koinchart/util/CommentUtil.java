package com.xuke.koinchart.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by kekex on 2018/9/5.
 */

public class CommentUtil {

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
}
