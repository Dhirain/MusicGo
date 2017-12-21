package com.dhirain.musicgo.ui.allSongs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.dhirain.musicgo.R;
import com.dhirain.musicgo.fileUtils.ReadWrite;
import com.dhirain.musicgo.model.MusicModel;
import com.dhirain.musicgo.ui.allSongs.view.ItemViewHolder;
import com.dhirain.musicgo.ui.home.MusicSelectListner;
import com.dhirain.musicgo.utills.ImageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dhirain Jain on 14-09-2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private static final String TAG = "ListAdapter";
    private boolean showOnlyOffline = false;
    private List<MusicModel> musicModels;
    private Context context;
    MusicModel currentMusic;
    private int lastPosition;
    private Long selectedItemPostion = -1L;
    private MusicSelectListner musicSelectListner;
    private ListChangeListner listChangeListner;

    public ListAdapter(Context context, MusicSelectListner musicSelectListner, boolean showOnlyOffline, ListChangeListner changeListner) {
        this.context = context;
        this.musicSelectListner = musicSelectListner;
        this.showOnlyOffline = showOnlyOffline;
        this.listChangeListner = changeListner;
    }

    public void setSelection(Long pos) {
        selectedItemPostion = pos;
        notifyDataSetChanged();
    }

    public void updateList(List<MusicModel> newRepo) {
        musicModels = new ArrayList<>();
        this.musicModels = newRepo;
        this.lastPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.music_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (musicModels != null) {
            showCurrentItem(holder, musicModels.get(position));
            //setAnimation(holder.parent,position);
        }
    }

    private void showCurrentItem(final ItemViewHolder holder, final MusicModel musicModel) {
        holder.song_artist.setText(musicModel.getArtists());
        holder.song_name.setText(musicModel.getSong());
        ImageUtils.setImage(context, musicModel.getCoverImage(), holder.song_cover, R.drawable.place_holder);

        if (musicModel.get_id().equals(selectedItemPostion)) {
            holder.song_indicator.setBackgroundColor(context.getResources().getColor(R.color.blue_light));
        } else {
            holder.song_indicator.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        if (showOnlyOffline) {
            holder.like.setVisibility(View.INVISIBLE);
        } else {
            holder.like.setImageResource(musicModel.isFavorite() ? R.drawable.like : R.drawable.unlike);
            holder.like.setOnClickListener(view -> {
                musicModel.setFavorite(!musicModel.isFavorite());
                holder.like.setImageResource(musicModel.isFavorite() ? R.drawable.like : R.drawable.unlike);
            });
        }

        holder.song_download.setImageResource(musicModel.isDownloaded() ? R.drawable.downloaded_true_icon : R.drawable.downloaded_false_icon);
        holder.song_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!musicModel.isDownloaded()) {
                    try {
                        ReadWrite.saveFile(musicModel.getUrl(), Long.toString(musicModel.get_id()), musicModel.getSong());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                musicModel.setDownloaded(true);
                holder.song_download.setImageResource(R.drawable.downloaded_true_icon);
            }
        });

        holder.song_cover.setOnClickListener(view -> {
            musicSelectListner.onMusicPlay(musicModel);
            setSelection(musicModel.get_id());
        });
        holder.song_artist.setOnClickListener(view -> {
            musicSelectListner.onMusicPlay(musicModel);
            setSelection(musicModel.get_id());
        });
        holder.song_name.setOnClickListener(view -> {
            musicSelectListner.onMusicPlay(musicModel);
            setSelection(musicModel.get_id());
        });

    }


    @Override
    public int getItemCount() {
        if (musicModels == null)
            return 0;
        else {
            return musicModels.size();
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
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


    public interface ListChangeListner {
        public void updateCurrentList(List<MusicModel> musicModels);
    }
}
