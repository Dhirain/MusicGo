package com.dhirain.musicgo.ui.home.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dhirain.musicgo.database.MusicGoDatabaseHelper;
import com.dhirain.musicgo.database.manager.DBManager;
import com.dhirain.musicgo.model.MusicModel;
import com.dhirain.musicgo.network.MusicService;
import com.dhirain.musicgo.ui.home.view.MainView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by Dhirain Jain on 19-12-2017.
 */

public class MainPresenter {
    private static String TAG = "MainPresenter";
    private Context context;
    private MainView mainView;
    private List<MusicModel> totalMusicList;
    private int currentPage = 0;
    private MusicGoDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public MainPresenter(Context context, MainView view) {
        this.context = context;
        this.mainView = view;
        dbHelper = new MusicGoDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void onViewAttached() {
        mainView.showProgress();
        MusicModel firstMusic = cupboard().withDatabase(db).query(MusicModel.class).get();
        if (firstMusic != null) {
            Log.d(TAG, "onViewAttached: from DB");
            fetchRepoFromDb();
        } else {
            Log.d(TAG, "onViewAttached: from network");
            fetchMusicFromNetworkOrCache();
        }

    }

    private void fetchRepoFromDb() {
        totalMusicList = DBManager.instance().getAllMusicFromDb();
        Log.d(TAG, "fetchRepoFromDb: "+totalMusicList.toString());
        mainView.updateList(totalMusicList);
        mainView.hideProgress();
    }

    private void fetchMusicFromNetworkOrCache(){
        final Call<List<MusicModel>> repos = MusicService.instance().getAllMusic();
        repos.enqueue(new Callback<List<MusicModel>>() {
            @Override
            public void onResponse(Call<List<MusicModel>> call, Response<List<MusicModel>> response) {
                if(response.isSuccessful()){
                    totalMusicList = response.body();
                    Log.d(TAG, "onResponse: "+totalMusicList.toString());
                    mainView.updateList(totalMusicList);
                    new StoringAsync().execute(totalMusicList);
                    mainView.hideProgress();
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
