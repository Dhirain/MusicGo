package com.dhirain.musicgo.ui.allSongs.view;

import com.dhirain.musicgo.model.MusicModel;

import java.util.List;

/**
 * Created by Dhirain Jain on 20-12-2017.
 */

public interface AllSongView {
    void showListState();

    void showEmptyState();

    void updateList(List<MusicModel> totalNewsList);

    void showProgress();

    void hideProgress();
}
