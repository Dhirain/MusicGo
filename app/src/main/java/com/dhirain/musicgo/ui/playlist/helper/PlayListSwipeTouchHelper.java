package com.dhirain.musicgo.ui.playlist.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.dhirain.musicgo.ui.playlist.adapter.PlayListAdapter;

/**
 * Created by DJ on 28-04-2017.
 */

public class PlayListSwipeTouchHelper extends ItemTouchHelper.SimpleCallback {
    PlayListAdapter mAdapter;

    public PlayListSwipeTouchHelper(PlayListAdapter mAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mAdapter = mAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.remove(viewHolder.getAdapterPosition());
    }
}
