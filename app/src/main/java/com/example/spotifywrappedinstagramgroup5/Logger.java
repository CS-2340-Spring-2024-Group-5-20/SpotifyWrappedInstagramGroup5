package com.example.spotifywrappedinstagramgroup5;

import android.util.Log;

/**
 * Helper class to log debug messages
 */
public class Logger {
    private static final String TAG = "BUG LOG";

    /**
     * Method to log a debug message to the LogCat console
     * @param message to be logged
     */
    public static void log(String message) {
        Log.d(TAG, message);
    }
}