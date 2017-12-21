package com.dhirain.musicgo.ui.allSongs.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dhirain.musicgo.database.MusicGoDatabaseHelper;
import com.dhirain.musicgo.database.manager.DBManager;
import com.dhirain.musicgo.model.MusicModel;
import com.dhirain.musicgo.network.MusicService;
import com.dhirain.musicgo.ui.allSongs.view.AllSongView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by Dhirain Jain on 20-12-2017.
 */

public class AllSongPresenter {
    private static final String TAG = "AllSongPresenter";
    private Context context;
    private List<MusicModel> unchangesNewsList;
    private List<MusicModel> totalMusicList;
    private List<MusicModel> currentNewsList;
    private int currentPage = 0;
    private AllSongView allSongView;
    private MusicGoDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public AllSongPresenter(Context context,AllSongView view ) {
        this.context = context;
        this.allSongView = view;
        dbHelper = new MusicGoDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public void onViewAttached() {
        allSongView.showProgress();
        MusicModel firstMusic = cupboard().withDatabase(db).query(MusicModel.class).get();
        if (firstMusic != null) {
            Log.d(TAG, "onViewAttached: from DB");
            fetchRepoFromDb();
        } else {
            Log.d(TAG, "onViewAttached: from network");
            fetchMusicFromNetworkOrCache();
        }

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
        allSongView.updateList(currentNewsList);
        /*currentNewsList = totalMusicList;
        homeScreenView.updateList(totalMusicList);*/
    }

    public void search(String query) {
        query = query.toLowerCase();

        final List<MusicModel> searchModelList = new ArrayList<>();
        for (MusicModel model : totalMusicList) {
            final String songs = model.getSong().toLowerCase();
            final String artist = model.getArtists().toLowerCase();
            if ((songs.contains(query.toLowerCase()) || artist.contains(query.toLowerCase())) && !searchModelList.contains(model)) {
                searchModelList.add(model);
            }
        }
        allSongView.updateList(searchModelList);

    }

    private void fetchRepoFromDb() {
        totalMusicList = DBManager.instance().getAllMusicFromDb();
        Log.d(TAG, "fetchRepoFromDb: "+totalMusicList.toString());
        allSongView.updateList(totalMusicList);
        allSongView.hideProgress();
    }

    public void fetchDownloaded(){
        totalMusicList = DBManager.instance().getDownloadedFromDb();
        allSongView.updateList(totalMusicList);
        allSongView.hideProgress();
    }

    private void fetchMusicFromNetworkOrCache(){
        final Call<List<MusicModel>> repos = MusicService.instance().getAllMusic();
        repos.enqueue(new Callback<List<MusicModel>>() {
            @Override
            public void onResponse(Call<List<MusicModel>> call, Response<List<MusicModel>> response) {
                if(response.isSuccessful()){
                    totalMusicList = response.body();
                    Log.d(TAG, "onResponse: "+totalMusicList.toString());
                    allSongView.updateList(totalMusicList);
                    new AllSongPresenter.StoringAsync().execute(totalMusicList);
                    allSongView.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<List<MusicModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private class StoringAsync extends AsyncTask<List<MusicModel>, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, "storing done", Toast.LENGTH_SHORT).show();
            // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected String doInBackground(List<MusicModel>... lists) {
            List<MusicModel> allNews = lists[0];
            MusicModel [] newsArray = new MusicModel[allNews.size()];
            newsArray = allNews.toArray(newsArray);
            for(MusicModel n: newsArray){
                cupboard().withDatabase(db).put(n);
            }

            return "done";
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
