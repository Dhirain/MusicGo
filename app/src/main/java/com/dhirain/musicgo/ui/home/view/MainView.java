package com.dhirain.musicgo.ui.home.view;

import com.dhirain.musicgo.model.MusicModel;

import java.util.List;

/**
 * Created by Dhirain Jain on 19-12-2017.
 */

public interface MainView {
    void gotoAllSongActivity();

    void gotoOfflineActivity();

    void gotoPlayListActivity();

    void showProgress();

    void hideProgress();

    void showNetworkFail();

    void updateList(List<MusicModel> totalNewsList);
}
