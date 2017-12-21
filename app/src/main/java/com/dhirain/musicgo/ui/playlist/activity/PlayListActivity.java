package com.dhirain.musicgo.ui.playlist.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhirain.musicgo.R;
import com.dhirain.musicgo.database.manager.StoreHistoryAsync;
import com.dhirain.musicgo.events.MediaStopDueToNoInternet;
import com.dhirain.musicgo.events.MusicCompletedListner;
import com.dhirain.musicgo.events.MusicStartedPlaying;
import com.dhirain.musicgo.media.MediaPlayService;
import com.dhirain.musicgo.model.MusicModel;
import com.dhirain.musicgo.model.TimeLineModel;
import com.dhirain.musicgo.ui.home.MusicSelectListner;
import com.dhirain.musicgo.ui.playlist.adapter.PlayListAdapter;
import com.dhirain.musicgo.ui.playlist.helper.PlayListSwipeTouchHelper;
import com.dhirain.musicgo.ui.playlist.presenter.PlaylistPresenter;
import com.dhirain.musicgo.ui.playlist.view.PlayListView;
import com.dhirain.musicgo.utills.Constants;
import com.dhirain.musicgo.utills.EndlessRecyclerViewScrollListener;
import com.dhirain.musicgo.utills.ImageUtils;
import com.dhirain.musicgo.utills.SharedPreferenceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import murgency.customer.ui.base.activity.BaseActivity;


/**
 * Created by Dhirain Jain on 20-12-2017.
 */

public class PlayListActivity extends BaseActivity implements PlayListView, MusicSelectListner, PlayListAdapter.ListChangeListner {
    private static final String TAG = "PlayListActivity";
    RecyclerView recyclerView;
    private PlayListAdapter playListAdapter;
    private RelativeLayout selected_tack_rr;
    private List<MusicModel> currentList;
    private PlaylistPresenter presenter;
    private TextView selected_track_author, selected_track_title;
    private ImageView selected_track_play, selected_track_previous, selected_track_next, selected_track_image;
    private Intent musicIntent;
    private MusicModel lastPlayedMusic;
    private ProgressDialog mProgressDialog;
    //private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        initUI();
        initAdapter();
        clickListener();
        setupPresenter();
    }

    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        playListAdapter = new PlayListAdapter(this, this, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(playListAdapter);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new mVideoList to the bottom of the list
                Log.d(TAG, "onLoadMore: paggination");
                presenter.paggination();
                //Toast.makeText(getContext(), "Adding items", Toast.LENGTH_SHORT).show();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        ItemTouchHelper.Callback mCallback = new PlayListSwipeTouchHelper(playListAdapter);
        ItemTouchHelper swipeTouchHelper = new ItemTouchHelper(mCallback);
        swipeTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.song_recycler);
        showListState();
        selected_tack_rr = findViewById(R.id.selected_track_rr);
        selected_track_author = findViewById(R.id.selected_track_author);
        selected_track_title = findViewById(R.id.selected_track_title);
        selected_track_play = findViewById(R.id.selected_play);
        selected_track_next = findViewById(R.id.selected_next);
        selected_track_previous = findViewById(R.id.selected_previous);
        selected_track_image = findViewById(R.id.selected_track_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intProgressbar();
        setTitleWithBackPress("Playlist");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showListState() {
        ImageView imageView = (ImageView) findViewById(R.id.no_result_found);
        imageView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyState() {
        ImageView imageView = (ImageView) findViewById(R.id.no_result_found);
        imageView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void clickListener() {
        selected_track_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.IS_SONG_PLAYING) {
                    Log.d(TAG, "onClick: pause");
                    selected_track_play.setImageResource(R.drawable.play_icon);
                    musicIntent = new Intent(PlayListActivity.this, MediaPlayService.class);
                    musicIntent.putExtra(Constants.TOPLAY, false);
                    musicIntent.putExtra(Constants.LOCATION, lastPlayedMusic.getUrl());
                    startService(musicIntent);
                    Constants.IS_SONG_PLAYING = false;
                } else {
                    selected_track_play.setImageResource(R.drawable.pause_icon);
                    playCurrentTrack(lastPlayedMusic);
                }
            }
        });

        selected_track_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextSong();
            }
        });

        selected_track_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPreviousSong();
            }
        });
    }

    private void playPreviousSong() {
        if (!currentList.isEmpty()) {
            lastPlayedMusic = getPreviousTrack();
            onMusicPlay(lastPlayedMusic);
        }
    }

    private void playNextSong() {
        Log.d(TAG, "onClick: next");
        Log.d(TAG, "onClick: allSongs" + currentList.toString());
        if (!currentList.isEmpty()) {
            lastPlayedMusic = getNextTrack();
            onMusicPlay(lastPlayedMusic);
        }
    }

    private MusicModel getNextTrack() {
        MusicModel next = currentList.get(0);
        if (lastPlayedMusic == null) {
            return next;
        }
        ListIterator<MusicModel> itr = currentList.listIterator();

        while (itr.hasNext()) {
            MusicModel currentItr = itr.next();
            if(currentItr.get_id().equals(lastPlayedMusic.get_id())){
                if(itr.hasNext())
                    next = itr.next();
                break;
            }
        }
        Log.d(TAG, "getNextTrack: " + next.toString());
        return next;
    }

    private MusicModel getPreviousTrack() {
        MusicModel prev = currentList.get(0);
        if (lastPlayedMusic == null) {
            return prev;
        }
        ListIterator<MusicModel> itr = currentList.listIterator();

        while (itr.hasNext()) {
            MusicModel currentItr = itr.next();
            if(currentItr.get_id().equals(lastPlayedMusic.get_id())){
                prev = itr.previous();
                if(itr.hasPrevious()){
                    prev = itr.previous();
                }
                break;
            }
        }
        Log.d(TAG, "getNextTrack: " + prev.toString());
        return prev;
    }



    @Override
    public void onMusicPlay(MusicModel musicModel) {
        Log.d(TAG, "onMusicPlay: " + musicModel.toString());
        lastPlayedMusic = musicModel;
        SharedPreferenceManager.singleton().saveLastMusic(Constants.LAST_PLAYED_MUSIC, musicModel);
        showCurrentTrack(musicModel);
        playCurrentTrack(musicModel);

    }

    private void playCurrentTrack(MusicModel musicModel) {
        Constants.IS_SONG_PLAYING = true;
        selected_track_play.setImageResource(R.drawable.loader_rotating);
        musicIntent = new Intent(this, MediaPlayService.class);
        musicIntent.putExtra(Constants.IS_DOWNLOADED, musicModel.isDownloaded());
        musicIntent.putExtra(Constants.TOPLAY, true);
        musicIntent.putExtra(Constants.LOCATION,
                musicModel.isDownloaded() ? Long.toString(musicModel.get_id()) : musicModel.getUrl());
        startService(musicIntent);
        new StoreHistoryAsync(this).execute(new TimeLineModel(musicModel.getSong(),musicModel.getArtists(),musicModel.getCoverImage(), Calendar.getInstance().getTimeInMillis()));
    }

    public void showCurrentTrack(MusicModel musicModel) {
        selected_tack_rr.setVisibility(View.VISIBLE);
        selected_track_title.setText(musicModel.getSong());
        selected_track_author.setText(musicModel.getArtists());
        selected_track_play.setImageResource(Constants.IS_SONG_PLAYING ? R.drawable.pause_icon : R.drawable.play_icon);
        ImageUtils.setImage(this, musicModel.getCoverImage(), selected_track_image, R.drawable.place_holder);
        playListAdapter.setSelection(lastPlayedMusic.get_id());
    }

    @Override
    public void setupPresenter() {
        presenter = new PlaylistPresenter(this, this);
        presenter.onViewAttached();
    }


    @Override
    public void updateList(List<MusicModel> totalNewsList) {
        currentList = totalNewsList;
        playListAdapter.updateList(totalNewsList);
        if (currentList.isEmpty()) {
            showEmptyState();
        } else {
            showListState();
        }
    }

    private void intProgressbar() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }


    @Override
    public void showProgress() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMusicStartedPlaying(MusicStartedPlaying event) {
        Log.d(TAG, "OnMusicStartedPlaying: ");
        selected_track_play.setImageResource(R.drawable.pause_icon);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMSteamingFailDueToNoInternet(MediaStopDueToNoInternet event) {
        Log.d(TAG, "MediaStopDueToNoInternet: ");
        selected_track_play.setImageResource(R.drawable.play_icon);
    }

    @Subscribe
    public void OnMusicCompleted(MusicCompletedListner event) {
        Log.d(TAG, "MusicCompletedListner: ");
        playNextSong();
    }

    @Override
    public void updateCurrentList(List<MusicModel> musicModels) {
        currentList = musicModels;
    }

    private void getLastPlayedSong() {
        MusicModel musicModel = SharedPreferenceManager.singleton().getLastMusic(Constants.LAST_PLAYED_MUSIC);
        if(musicModel != null){
            lastPlayedMusic = musicModel;
            showCurrentTrack(lastPlayedMusic);
            if (Constants.IS_SONG_PLAYING) {
                playListAdapter.setSelection(musicModel.get_id());
            }
        }
        else {
            selected_tack_rr.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLastPlayedSong();
    }
}
