package com.xkf.guessmusicgame.util;

import android.util.Log;

public class MyLog {
    public static final boolean DEBUG = true;

    public static void debug(String tag, String message) {
        Log.d(tag, message);
    }

    public static void error(String tag, String message) {
        Log.e(tag, message);
    }

    public static void wrong(String tag, String message) {
        Log.w(tag, message);
    }

    public static void info(String tag, String message) {
        Log.i(tag, message);
    }

}
