package com.dhirain.musicgo.ui.playlist.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dhirain.musicgo.database.MusicGoDatabaseHelper;
import com.dhirain.musicgo.database.manager.DBManager;
import com.dhirain.musicgo.model.MusicModel;
import com.dhirain.musicgo.ui.playlist.view.PlayListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhirain Jain on 20-12-2017.
 */

public class PlaylistPresenter {
    private static final String TAG = "AllSongPresenter";
    private Context context;
    private List<MusicModel> unchangesNewsList;
    private List<MusicModel> totalMusicList;
    private List<MusicModel> currentNewsList;
    private int currentPage = 0;
    private PlayListView playListView;
    private MusicGoDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public PlaylistPresenter(Context context, PlayListView view ) {
        this.context = context;
        this.playListView = view;
        dbHelper = new MusicGoDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public void onViewAttached() {
        playListView.showProgress();
        fetchFavaroite();

    }

    public void paggination() {
        if (currentNewsList == null) {
            currentPage = 0;
            currentNewsList = new ArrayList<>();
        }
        int tillPage = currentPage + 20;
        while (currentPage < tillPage && currentPage < totalMusicList.size()) {
            currentNewsList.add(totalMusicList.get(currentPage));
            currentPage++;
        }
        Log.d(TAG, "paggination: " + currentPage);
        playListView.updateList(currentNewsList);
        /*currentNewsList = totalMusicList;
        homeScreenView.updateList(totalMusicList);*/
    }



    public void fetchFavaroite(){
        totalMusicList = DBManager.instance().getFavFromDb();
        paggination();
        playListView.hideProgress();
    }


}
