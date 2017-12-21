package com.dhirain.musicgo.ui.home.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhirain.musicgo.R;
import com.dhirain.musicgo.database.manager.StoreHistoryAsync;
import com.dhirain.musicgo.events.MediaStopDueToNoInternet;
import com.dhirain.musicgo.events.MusicStartedPlaying;
import com.dhirain.musicgo.media.MediaPlayService;
import com.dhirain.musicgo.model.MusicModel;
import com.dhirain.musicgo.model.TimeLineModel;
import com.dhirain.musicgo.ui.allSongs.activity.AllSongsActivity;
import com.dhirain.musicgo.ui.history.activity.TimeLineActivity;
import com.dhirain.musicgo.ui.home.MusicSelectListner;
import com.dhirain.musicgo.ui.home.adapter.ArtistAdapter;
import com.dhirain.musicgo.ui.home.adapter.TrendingAdapter;
import com.dhirain.musicgo.ui.home.presenter.MainPresenter;
import com.dhirain.musicgo.ui.home.view.MainView;
import com.dhirain.musicgo.ui.playlist.activity.PlayListActivity;
import com.dhirain.musicgo.utills.Constants;
import com.dhirain.musicgo.utills.ImageUtils;
import com.dhirain.musicgo.utills.SharedPreferenceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.List;

import murgency.customer.ui.base.activity.BaseActivity;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView, MusicSelectListner {

    private static final String TAG = "MainActivity";
    private ProgressDialog mProgressDialog;
    private NavigationView navigationView;
    private MainPresenter presenter;
    private RecyclerView trendingRecycler,artistRecycler;
    private TrendingAdapter trendingAdapter;
    private ArtistAdapter artistAdapter;
    private Intent musicIntent;
    private RelativeLayout selected_tack_rr;
    private TextView selected_track_author, selected_track_title,see_all_tv;
    private ImageView selected_track_control, selected_track_image;
    //private boolean isPlaying = false;
    private MusicModel lastPlayedMusic;
    private List<MusicModel> totalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        setupPresenter();
        clickListener();
    }

    private void initAdapter() {
        trendingRecycler.setHasFixedSize(true);
        trendingAdapter = new TrendingAdapter(this, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        trendingRecycler.setLayoutManager(manager);
        trendingRecycler.setAdapter(trendingAdapter);

        artistRecycler.setHasFixedSize(true);
        artistAdapter = new ArtistAdapter(this, this);
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        artistRecycler.setLayoutManager(manager1);
        artistRecycler.setAdapter(artistAdapter);
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
        selected_track_control.setImageResource(R.drawable.pause_icon);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMSteamingFailDueToNoInternet(MediaStopDueToNoInternet event) {
        Log.d(TAG, "MediaStopDueToNoInternet: ");
        selected_track_control.setImageResource(R.drawable.play_icon);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_songs) {
            gotoAllSongActivity();
            // Handle the camera action
        } else if (id == R.id.offline_mode) {
            gotoOfflineActivity();
        }
        else if(id == R.id.playlist){
            gotoPlayListActivity();
        }
        else if(id == R.id.history){
            gotoTimelineActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        trendingRecycler = findViewById(R.id.trendingRecycler);
        artistRecycler = findViewById(R.id.artist_recycler);

        selected_tack_rr = findViewById(R.id.selected_track_rr);
        selected_track_author = findViewById(R.id.selected_track_author);
        selected_track_title = findViewById(R.id.selected_track_title);
        selected_track_control = findViewById(R.id.selected_track_control);
        selected_track_image = findViewById(R.id.selected_track_image);
        see_all_tv = findViewById(R.id.sell_all_tv);
        intProgressbar();
        initAdapter();

    }

    @Override
    public void clickListener() {
        navigationView.setNavigationItemSelectedListener(this);
        selected_track_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.IS_SONG_PLAYING) {
                    Log.d(TAG, "onClick: pause");
                    selected_track_control.setImageResource(R.drawable.play_icon);
                    musicIntent = new Intent(MainActivity.this, MediaPlayService.class);
                    musicIntent.putExtra(Constants.TOPLAY, false);
                    musicIntent.putExtra(Constants.LOCATION, lastPlayedMusic.getUrl());
                    startService(musicIntent);
                    Constants.IS_SONG_PLAYING = false;
                } else {
                    selected_track_control.setImageResource(R.drawable.pause_icon);
                    playCurrentTrack(lastPlayedMusic);
                }

            }
        });
        see_all_tv.setOnClickListener(view -> gotoAllSongActivity());
    }

    @Override
    public void gotoAllSongActivity() {
        Intent intent = new Intent(MainActivity.this, AllSongsActivity.class);
        //intent.putExtra(Constants.IS_PLAYING, isPlaying);
        startActivity(intent);
    }

    @Override
    public void gotoOfflineActivity() {
        Intent intent = new Intent(MainActivity.this, AllSongsActivity.class);
        intent.putExtra(Constants.SHOW_ONLY_FAV,true);
        //intent.putExtra(Constants.IS_PLAYING, isPlaying);
        startActivity(intent);
    }

    @Override
    public void gotoPlayListActivity() {
        Intent intent = new Intent(MainActivity.this, PlayListActivity.class);
        startActivity(intent);
    }

    private void gotoTimelineActivity() {
        Intent intent = new Intent(MainActivity.this, TimeLineActivity.class);
        startActivity(intent);
    }

    @Override
    public void setupPresenter() {
        presenter = new MainPresenter(this, this);
        presenter.onViewAttached();
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
    public void showNetworkFail() {
        Snackbar.make(findViewById(R.id.constraint_layout), "Network failure Opps :-(", Snackbar.LENGTH_LONG);
    }

    @Override
    public void updateList(@Nullable List<MusicModel> musics) {
        totalList = musics;
        trendingAdapter.updateList(musics);
        artistAdapter.updateList(musics);
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
        selected_track_control.setImageResource(R.drawable.loader_rotating);
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
        selected_track_control.setImageResource(Constants.IS_SONG_PLAYING ? R.drawable.pause_icon : R.drawable.play_icon);
        ImageUtils.setImage(this, musicModel.getCoverImage(), selected_track_image, R.drawable.place_holder);
    }

    private void getLastPlayedSong() {
        MusicModel musicModel = SharedPreferenceManager.singleton().getLastMusic(Constants.LAST_PLAYED_MUSIC);
        if(musicModel != null){
            lastPlayedMusic = musicModel;
            showCurrentTrack(lastPlayedMusic);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
