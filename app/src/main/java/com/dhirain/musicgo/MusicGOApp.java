package com.dhirain.musicgo;

import android.app.Application;
import android.content.Context;

/**
 * Created by DJ on 09-09-2017.
 */

public class MusicGOApp extends Application {

    private static MusicGOApp sSingleton;

    public static MusicGOApp singleton() {
        return sSingleton;
    }

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        sSingleton = this;
        context = getApplicationContext();
    }

    public Context getContext() {
        return context;
    }
}
