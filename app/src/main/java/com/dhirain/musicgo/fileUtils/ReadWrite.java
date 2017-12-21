package com.dhirain.musicgo.fileUtils;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.dhirain.musicgo.MusicGOApp;
import com.dhirain.musicgo.R;
import com.dhirain.musicgo.utills.GenericCallback;
import com.koushikdutta.ion.Ion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by DJ on 03-06-2017.
 */

public class ReadWrite {

    public static void saveFile(String url, String fileName, String song) throws IOException {
        NotificationManager notificationManager = (NotificationManager)
                MusicGOApp.singleton().getContext().getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MusicGOApp.singleton().getContext())
                .setSmallIcon(R.drawable.downloaded_true_icon)
                .setContentTitle("Download")
                .setContentText("Downloading song "+song+"... ")
                .setAutoCancel(true);
        notificationManager.notify(Integer.valueOf(fileName), notificationBuilder.build());

        if(FileUtil.isExternalStorageWritable() && FileUtil.isExternalStorageReadable()){
            final Context context  = MusicGOApp.singleton().getContext();
            Ion.with(context)
                    .load(url)
                    .write(new File(ReadWrite.getAppDirectory(),fileName+FileUtil.MP3))
                    .setCallback((e, file) -> {
                        if(file == null){
                            Toast.makeText(context, "Something went wrong :(", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(context, "download completed", Toast.LENGTH_SHORT).show();
                    });
        }
        else {
            Toast.makeText(MusicGOApp.singleton().getContext(), "SD card not found", Toast.LENGTH_SHORT).show();
        }
    }

    public static String readFile(String fileName) throws IOException {
        String myData = "";
        if(FileUtil.isExternalStorageWritable() && FileUtil.isExternalStorageReadable()){
                File file = new File(ReadWrite.getAppDirectory(),fileName+FileUtil.MP3);
                FileInputStream fis = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    myData = myData + strLine;
                }
                in.close();
        }
        else {
            Toast.makeText(MusicGOApp.singleton().getContext(), "SD card not found", Toast.LENGTH_SHORT).show();
            throw  new IOException("SD card not found");
        }
        return myData;
    }


    private static File getAppDirectory(){

        String root = Environment.getExternalStorageDirectory().toString();

        File myDir = new File(root + FileUtil.FOLDER_NAME);

        myDir.mkdirs();

        return myDir;

    }

    public static void readFile(final String fileName, GenericCallback callback)    {
        Single.fromCallable(() -> readFile(fileName))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (data) -> callback.onRequestSuccess(data),
                        (err) -> callback.onRequestFailure(err, err.getMessage())
                );
    }

}
