package com.zxq.myokhttp.util;

import android.content.Context;

/**
 *
 */
public class Global {

    private static Context mContext;
    private static String userUnique;

    public static void initConfig(Context context, String unique) {
        mContext = context;
        userUnique = unique;
    }


    public static void setUserUnique(String userUnique) {
        Global.userUnique = userUnique;
    }

    public static Context getmContext() {
        return mContext;
    }

    public static String getUserUnique() {
        return userUnique;
    }

    public static long getTIMESTAMP() {
        long TIMESTAMP = System.currentTimeMillis();
        TIMESTAMP = TIMESTAMP/1000;
        return TIMESTAMP;
    }

}






















