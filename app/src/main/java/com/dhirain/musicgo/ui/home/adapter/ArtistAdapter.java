package com.dhirain.musicgo.ui.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhirain.musicgo.R;
import com.dhirain.musicgo.model.MusicModel;
import com.dhirain.musicgo.ui.home.MusicSelectListner;
import com.dhirain.musicgo.ui.home.view.ArtistTileViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Dhirain Jain on 19-12-2017.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistTileViewHolder>  {
    private static final String TAG = "TrendingAdapter";
    private List<MusicModel> musicList;
    private Context context;
    private MusicSelectListner musicSelectListner;
    private MusicModel atif_aslam, rahat_fate_ali, harshadeep;

    public ArtistAdapter(Context context, MusicSelectListner musicSelectListner) {
        this.context = context;
        this.musicSelectListner = musicSelectListner;
    }

    public void updateList(List<MusicModel> musicRepo) {
        musicList = new ArrayList<>();
        this.musicList = musicRepo;
        setArtist();
        notifyDataSetChanged();
    }

    private void setArtist() {
        ListIterator<MusicModel> iterator = musicList.listIterator();
        while (iterator.hasNext()){
            MusicModel cur = iterator.next();
            if(cur.getArtists().contains("Rahat Fateh Ali")){
                Log.d(TAG, "setArtist: rahat "+cur.toString());
                rahat_fate_ali=cur;
            }
            else if(cur.getArtists().contains("Atif Aslam")){
                Log.d(TAG, "setArtist: atif "+cur.toString());
                atif_aslam =cur;
            }
            else if (cur.getArtists().contains("Harshadeep")){
                Log.d(TAG, "setArtist: Harsha "+cur.toString());
                harshadeep =cur;
            }
        }
    }

    @Override
    public ArtistTileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_tile, parent, false);
        return new ArtistTileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArtistTileViewHolder holder, int position) {
        if (musicList != null) {
            showCurrentItem(holder,position);
        }
    }

    private void showCurrentItem(ArtistTileViewHolder holder, int position) {
        switch (position){
            case 0:
                holder.song_artist.setText("Atif Aslam");
                holder.songIV.setImageResource(R.drawable.atif);
                holder.songIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicSelectListner.onMusicPlay(atif_aslam);
                    }
                });
                break;
            case 1:
                holder.song_artist.setText("Rahat Fateh Ali");
                holder.songIV.setImageResource(R.drawable.rahat_fate);
                holder.songIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicSelectListner.onMusicPlay(rahat_fate_ali);
                    }
                });
                break;
            default:
            case 2:
                holder.song_artist.setText("Harshadeep Kaur");
                holder.songIV.setImageResource(R.drawable.harshdeep);
                holder.songIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicSelectListner.onMusicPlay(harshadeep);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(musicList == null)
            return 0;
        else
        return 3;
    }
}
