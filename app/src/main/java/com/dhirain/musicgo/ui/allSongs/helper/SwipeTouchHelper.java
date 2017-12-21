package com.dhirain.musicgo.ui.allSongs.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.dhirain.musicgo.ui.allSongs.adapter.ListAdapter;

/**
 * Created by DJ on 28-04-2017.
 */

public class SwipeTouchHelper extends ItemTouchHelper.SimpleCallback {
    ListAdapter mAdapter;

    public SwipeTouchHelper(ListAdapter mAdapter) {
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
