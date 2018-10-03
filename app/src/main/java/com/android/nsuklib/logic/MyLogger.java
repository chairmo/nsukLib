package com.android.nsuklib.logic;

import com.android.nsuklib.BuildConfig;

public class MyLogger {

    private static final boolean LOG = BuildConfig.DEBUG;
    public static void i(String tag, String string) {
        if (LOG) android.util.Log.i(tag, string);
    }
    public static void e(String tag, String string) {
        if (LOG) android.util.Log.e(tag, string);
    }
    public static void d(String tag, String string) {
        if (LOG) android.util.Log.d(tag, string);
    }
}
