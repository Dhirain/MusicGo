package com.dhirain.musicgo.ui.playlist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhirain.musicgo.R;
import com.dhirain.musicgo.model.MusicModel;
import com.dhirain.musicgo.ui.home.MusicSelectListner;
import com.dhirain.musicgo.ui.playlist.view.PlaylistViewHolder;
import com.dhirain.musicgo.utills.ImageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dhirain Jain on 14-09-2017.
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {
    private static final String TAG = "ListAdapter";
    private List<MusicModel> musicModels;
    private Context context;
    MusicModel currentMusic;
    private int lastPosition;
    private Long selectedItemPostion = -1L;
    private MusicSelectListner musicSelectListner;
    private ListChangeListner listChangeListner;

    public PlayListAdapter(Context context, MusicSelectListner musicSelectListner,ListChangeListner listChangeListner) {
        this.context = context;
        this.musicSelectListner = musicSelectListner;
        this.listChangeListner = listChangeListner;
    }



    public void updateList(List<MusicModel> newRepo) {
        musicModels = new ArrayList<>();
        this.musicModels = newRepo;
        this.lastPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item, parent, false);
        return new PlaylistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {
        if (musicModels != null) {
            showCurrentItem(holder, musicModels.get(position),position);
            //setAnimation(holder.parent,position);
        }
    }

    private void showCurrentItem(final PlaylistViewHolder holder, final MusicModel musicModel, int pos) {
        holder.song_artist.setText(musicModel.getArtists());
        holder.song_name.setText(musicModel.getSong());
        ImageUtils.setImage(context,musicModel.getCoverImage(),holder.song_cover,R.drawable.place_holder);
        if(musicModel.get_id().equals(selectedItemPostion)){
            holder.song_indicator.setBackgroundColor(context.getResources().getColor(R.color.blue_light));
        }
        else {
            holder.song_indicator.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.song_cover.setOnClickListener(
                view -> {
                    musicSelectListner.onMusicPlay(musicModel);
                    setSelection(musicModel.get_id());
                }
        );
        holder.song_artist.setOnClickListener(
                view -> {
                    musicSelectListner.onMusicPlay(musicModel);
                    setSelection(musicModel.get_id());
                }
        );
        holder.song_name.setOnClickListener( view -> {
            musicSelectListner.onMusicPlay(musicModel);
            setSelection(musicModel.get_id());
        });

        holder.like.setImageResource(musicModel.isFavorite() ? R.drawable.like: R.drawable.unlike);
        holder.like.setOnClickListener(view -> {
            musicModel.setFavorite(!musicModel.isFavorite());
            holder.like.setImageResource(musicModel.isFavorite() ? R.drawable.like : R.drawable.unlike);
        });

    }

    public void setSelection(Long pos) {
        selectedItemPostion = pos;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (musicModels == null)
            return 0;
        else {
            return musicModels.size();
        }
    }


    public void swap(int adapterPosition, int adapterPosition1) {
        Collections.swap(musicModels, adapterPosition, adapterPosition1);
        listChangeListner.updateCurrentList(musicModels);
        notifyItemMoved(adapterPosition, adapterPosition1);
    }

    public void remove(int adapterPosition) {
        long id = musicModels.get(adapterPosition).get_id();
        musicModels.remove(adapterPosition);
        listChangeListner.updateCurrentList(musicModels);
        notifyItemRemoved(adapterPosition);
    }

    public interface ListChangeListner{
        public void updateCurrentList(List<MusicModel> musicModels);
    }
}
