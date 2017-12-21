package com.dhirain.musicgo.media;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dhirain.musicgo.events.MediaStopDueToNoInternet;
import com.dhirain.musicgo.events.MusicCompletedListner;
import com.dhirain.musicgo.events.MusicStartedPlaying;
import com.dhirain.musicgo.fileUtils.FileUtil;
import com.dhirain.musicgo.network.NetworkUtills;
import com.dhirain.musicgo.utills.Constants;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by Dhirain Jain on 20-12-2017.
 */

public class MediaPlayService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = "MediaPlayService";
    public MediaPlayer mediaPlayer;
    private boolean isStreaming;
    private String location;
    private boolean toPlay;
    private boolean isDownloaded;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        toPlay = intent.getBooleanExtra(Constants.TOPLAY, false);
        if (!toPlay) {
            mediaPlayer.pause();
        }
        else {
            isDownloaded = intent.getBooleanExtra(Constants.IS_DOWNLOADED, false);
            location = intent.getStringExtra(Constants.LOCATION);
            if (isDownloaded) {
                String filePath = Environment.getExternalStorageDirectory() + FileUtil.FOLDER_NAME + "/" + location + FileUtil.MP3;
                location = filePath;
                Log.d(TAG, "onStartCommand: isDownloaded " + filePath);
            } else {
                if (!NetworkUtills.isNetworkAvailable()) {
                    EventBus eventBus = EventBus.getDefault();
                    MediaStopDueToNoInternet noInternet = new MediaStopDueToNoInternet();
                    eventBus.post(noInternet);
                }
            }

            Log.d(TAG, "onStartCommand: ");
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.reset();
                Log.d(TAG, "onStartCommand: " + "setdatasource");
                mediaPlayer.setDataSource(location);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        mediaPlayer.setOnPreparedListener(this);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        EventBus eventBus = EventBus.getDefault();
        MusicCompletedListner completedListner = new MusicCompletedListner();
        eventBus.post(completedListner);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.d(TAG, "onError: ");
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        EventBus eventBus = EventBus.getDefault();
        MusicStartedPlaying skipEvent = new MusicStartedPlaying();
        eventBus.post(skipEvent);
        Log.d(TAG, "onPrepared: " + "start");
        mediaPlayer.setLooping(true);
    }
}
