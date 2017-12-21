package com.dhirain.musicgo.database.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.dhirain.musicgo.database.MusicGoDatabaseHelper;
import com.dhirain.musicgo.model.TimeLineModel;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by Dhirain Jain on 21-12-2017.
 */

public class StoreHistoryAsync extends AsyncTask<TimeLineModel, Void, String> {
    private static final String TAG = "StoreHistoryAsync";
    private Context context;
    MusicGoDatabaseHelper dbHelper;
    SQLiteDatabase db;

    public StoreHistoryAsync(Context context) {
        this.context = context;
        dbHelper = new MusicGoDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute: ");
        // txt.setText(result);
        // might want to change "executed" for the returned string passed
        // into onPostExecute() but that is upto you
    }


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Void... values) {}


    @Override
    protected String doInBackground(TimeLineModel... timeLineModels) {

        TimeLineModel timeLineModel = timeLineModels[0];
        Log.d(TAG, "doInBackground: "+timeLineModel.toString());
        long id=cupboard().withDatabase(db).put(timeLineModel);
        Log.d(TAG, "doInBackground: "+id);
        return "done";
    }
}
