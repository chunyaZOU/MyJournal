package com.example.h2601864.myjournal.utilities;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by H2601864 on 2015/11/24.
 */
public class CommonUtil {

    /**
     * dp 2 px
     * @param c
     * @param dp
     * @return int
     */
    public static int dp2px(Context c,int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }
}
