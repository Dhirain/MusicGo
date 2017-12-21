package com.dhirain.musicgo.ui.history.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dhirain.musicgo.database.MusicGoDatabaseHelper;
import com.dhirain.musicgo.database.manager.DBManager;
import com.dhirain.musicgo.model.TimeLineModel;
import com.dhirain.musicgo.ui.history.view.TimeLineView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhirain Jain on 20-12-2017.
 */

public class TimeLinePresenter {
    private static final String TAG = "AllSongPresenter";
    private Context context;
    private List<TimeLineModel> totalMusicList;
    private List<TimeLineModel> currentNewsList;
    private int currentPage = 0;
    private TimeLineView allSongView;
    private MusicGoDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public TimeLinePresenter(Context context, TimeLineView view ) {
        this.context = context;
        this.allSongView = view;
        dbHelper = new MusicGoDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public void onViewAttached() {
        allSongView.showProgress();
        fetchRepoFromDb();
    }

    public void paggination() {
        if (currentNewsList == null) {
            currentPage = 0;
            currentNewsList = new ArrayList<>();
        }
        int tillPage = currentPage + 10;
        while (currentPage < tillPage && currentPage < totalMusicList.size()) {
            currentNewsList.add(totalMusicList.get(currentPage));
            currentPage++;
        }
        Log.d(TAG, "paggination: " + currentPage);
        allSongView.updateList(currentNewsList);
    }


    private void fetchRepoFromDb() {
        totalMusicList = DBManager.instance().getAllHistoryDb();
        Log.d(TAG, "fetchRepoFromDb: "+totalMusicList.toString());
        paggination();
        allSongView.hideProgress();
    }


}
