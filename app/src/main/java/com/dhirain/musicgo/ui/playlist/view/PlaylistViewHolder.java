package com.dhirain.musicgo.ui.playlist.view;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhirain.musicgo.R;


/**
 * Created by DJ on 14-09-2017.
 */

public class PlaylistViewHolder extends RecyclerView.ViewHolder {
    public TextView song_name, song_artist;
    public ImageView like,song_cover;
    public RelativeLayout song_indicator;
    public LinearLayout meta;
    public RelativeLayout parent;
    public CardView cardView;

    public PlaylistViewHolder(View itemView) {
        super(itemView);
        song_name = (TextView) itemView.findViewById(R.id.song_title);
        song_artist = (TextView) itemView.findViewById(R.id.song_artist);
        song_cover = (ImageView) itemView.findViewById(R.id.song_cover);
        like = (ImageView) itemView.findViewById(R.id.like);
        song_indicator = itemView.findViewById(R.id.song_indicator);
        /*
        parent = (RelativeLayout) itemView.findViewById(R.id.parentRR);
        meta = (LinearLayout) itemView.findViewById(R.id.meta_detail);
        cardView = (CardView) itemView.findViewById(R.id.cardView);*/
    }
}
