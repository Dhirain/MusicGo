package com.dhirain.musicgo.ui.home.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhirain.musicgo.R;

/**
 * Created by Dhirain Jain on 20-12-2017.
 */
public class ArtistTileViewHolder extends RecyclerView.ViewHolder {
    public ImageView songIV;
    public TextView song_artist;

    public ArtistTileViewHolder(View itemView) {
        super(itemView);
        songIV = itemView.findViewById(R.id.music_image);
        song_artist = itemView.findViewById(R.id.song_artist);
    }
}
