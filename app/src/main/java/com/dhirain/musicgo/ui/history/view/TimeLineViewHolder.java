package com.dhirain.musicgo.ui.history.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhirain.musicgo.R;
import com.github.vipulasri.timelineview.TimelineView;


/**
 * Created by DJ on 14-09-2017.
 */

public class TimeLineViewHolder extends RecyclerView.ViewHolder {
    public TextView song_name, song_artist, song_time;
    public ImageView song_cover;
    public TimelineView timelineView;
    public RelativeLayout parent;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        timelineView = itemView.findViewById(R.id.time_marker);
        timelineView.initLine(viewType);
        song_name = (TextView) itemView.findViewById(R.id.song_title);
        song_artist = (TextView) itemView.findViewById(R.id.song_artist);
        song_cover = (ImageView) itemView.findViewById(R.id.song_cover);
        song_time = itemView.findViewById(R.id.song_time);

    }
}
