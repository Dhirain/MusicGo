package com.dhirain.musicgo.fileUtils;

import android.os.Environment;

/**
 * Created by DJ on 14-09-2017.
 */

public class FileUtil {
    public  static final String FOLDER_NAME = "/MusicGo";
    public static final String MP3 = ".mp3";

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
