package com.dhirain.musicgo.database.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dhirain.musicgo.MusicGOApp;
import com.dhirain.musicgo.database.MusicGoDatabaseHelper;
import com.dhirain.musicgo.model.MusicModel;
import com.dhirain.musicgo.model.TimeLineModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by DJ on 14-09-2017.
 */

public class DBManager {

    private static DBManager sSingleton;
    private Context mContext;
    private MusicGoDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public static synchronized DBManager instance() {
        if (sSingleton == null) {
            sSingleton = new DBManager();
        }

        return sSingleton;
    }

    private DBManager() {
        mContext= MusicGOApp.singleton().getContext();
        dbHelper= new MusicGoDatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
    }

    public List<MusicModel> getAllMusicFromDb() {
        List<MusicModel> repos = new ArrayList<>();
        QueryResultIterable<MusicModel> itr = cupboard().withDatabase(db).query(MusicModel.class).query();
        Log.d("Database", "From db");
        for (MusicModel s : itr)
            repos.add(s);
        return repos;
    }

    public List<TimeLineModel> getAllHistoryDb() {
        List<TimeLineModel> repos = new ArrayList<>();
        QueryResultIterable<TimeLineModel> itr = cupboard().withDatabase(db).query(TimeLineModel.class).orderBy("time").query();
        Log.d("Database", "From db");
        for (TimeLineModel s : itr)
            repos.add(s);
        Collections.reverse(repos);
        return repos;
    }

    public List<MusicModel> getFavFromDb() {
        List<MusicModel> repos = new ArrayList<>();
        QueryResultIterable<MusicModel> itr = cupboard().
                withDatabase(db).
                query(MusicModel.class)
                .withSelection("isFavorite = ?", Integer.toString(1))
                .query();
        Log.d("Database", "From fav db");
        for (MusicModel s : itr)
            repos.add(s);
        return repos;
    }

    public List<MusicModel> getDownloadedFromDb() {
        List<MusicModel> repos = new ArrayList<>();
        QueryResultIterable<MusicModel> itr = cupboard().
                withDatabase(db).
                query(MusicModel.class)
                .withSelection("isDownloaded = ?", Integer.toString(1))
                .query();
        Log.d("Database", "From isDownloaded db");
        for (MusicModel s : itr)
            repos.add(s);
        return repos;
    }

    public int updateFavToItem(Long s_no,boolean updateToValue){
        ContentValues values = new ContentValues(1);
        values.put("isFavorite", updateToValue);
        return cupboard()
                .withDatabase(db)
                .update(MusicModel.class, values, "_id = ?", String.valueOf(s_no));
    }

    public int updateDownloadToItem(Long s_no,boolean updateToValue){
        ContentValues values = new ContentValues(1);
        values.put("isDownloaded", updateToValue);
        return cupboard()
                .withDatabase(db)
                .update(MusicModel.class, values, "_id = ?", String.valueOf(s_no));
    }

}
