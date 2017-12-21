package com.dhirain.musicgo.ui.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhirain.musicgo.R;
import com.dhirain.musicgo.model.MusicModel;
import com.dhirain.musicgo.ui.home.MusicSelectListner;
import com.dhirain.musicgo.ui.home.view.MusictileViewHolder;
import com.dhirain.musicgo.utills.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhirain Jain on 19-12-2017.
 */

public class TrendingAdapter extends RecyclerView.Adapter<MusictileViewHolder>  {
    private static final String TAG = "TrendingAdapter";
    private List<MusicModel> musicList;
    private Context context;
    private MusicSelectListner musicSelectListner;

    public TrendingAdapter(Context context, MusicSelectListner musicSelectListner) {
        this.context = context;
        this.musicSelectListner = musicSelectListner;
    }

    public void updateList(List<MusicModel> musicRepo) {
        musicList = new ArrayList<>();
        this.musicList = musicRepo;
        notifyDataSetChanged();
    }

    @Override
    public MusictileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.music_tile, parent, false);
        return new MusictileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MusictileViewHolder holder, int position) {
        if (musicList != null) {
            showCurrentItem(holder, musicList.get(position));
        }
    }

    private void showCurrentItem(MusictileViewHolder holder, MusicModel music) {
        holder.song_title.setText(music.getSong());
        holder.song_artist.setText(music.getArtists());
        ImageUtils.setImage(context,music.getCoverImage(),holder.songIV,R.drawable.black_rectangle);
        holder.songIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicSelectListner.onMusicPlay(music);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(musicList == null)
            return 0;
        else
        return musicList.size();
    }
}
