package com.dhirain.musicgo.ui.history.view;

import com.dhirain.musicgo.model.TimeLineModel;

import java.util.List;

/**
 * Created by Dhirain Jain on 20-12-2017.
 */

public interface TimeLineView {
    void showListState();

    void showEmptyState();

    void updateList(List<TimeLineModel> totalNewsList);

    void showProgress();

    void hideProgress();
}
