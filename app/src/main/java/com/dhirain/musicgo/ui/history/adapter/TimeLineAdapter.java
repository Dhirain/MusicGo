package com.dhirain.musicgo.ui.history.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhirain.musicgo.R;
import com.dhirain.musicgo.model.TimeLineModel;
import com.dhirain.musicgo.ui.history.view.TimeLineViewHolder;
import com.dhirain.musicgo.utills.DateUtil;
import com.dhirain.musicgo.utills.ImageUtils;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhirain Jain on 14-09-2017.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {
    private static final String TAG = "ListAdapter";
    private List<TimeLineModel> musicModels;
    private Context context;


    public TimeLineAdapter(Context context) {
        this.context = context;
    }

    public void updateList(List<TimeLineModel> newRepo) {
        musicModels = new ArrayList<>();
        this.musicModels = newRepo;
        notifyDataSetChanged();
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_item, parent, false);
        return new TimeLineViewHolder(itemView,viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        if (musicModels != null) {
            showCurrentItem(holder, musicModels.get(position),position);
        }
    }

    private void showCurrentItem(final TimeLineViewHolder holder, final TimeLineModel musicModel, int pos) {
        holder.song_artist.setText(musicModel.getArtists());
        holder.song_name.setText(musicModel.getSong());
        holder.song_time.setText(DateUtil.getUTCString(musicModel.getTime()));
        ImageUtils.setImage(context,musicModel.getCoverImage(),holder.song_cover,R.drawable.place_holder);
        holder.timelineView.setMarker((pos == 0)?context.getResources().getDrawable(R.drawable.ic_marker_active):context.getResources().getDrawable(R.drawable.ic_marker_inactive));
    }


    @Override
    public int getItemCount() {
        if (musicModels == null)
            return 0;
        else {
            return musicModels.size();
        }
    }
}
