package com.dhirain.musicgo.ui.history.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.dhirain.musicgo.R;
import com.dhirain.musicgo.model.TimeLineModel;
import com.dhirain.musicgo.ui.history.adapter.TimeLineAdapter;
import com.dhirain.musicgo.ui.history.presenter.TimeLinePresenter;
import com.dhirain.musicgo.ui.history.view.TimeLineView;
import com.dhirain.musicgo.utills.EndlessRecyclerViewScrollListener;

import java.util.List;

import murgency.customer.ui.base.activity.BaseActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by Dhirain Jain on 20-12-2017.
 */

public class TimeLineActivity extends BaseActivity implements TimeLineView {
    RecyclerView recyclerView;
    private TimeLineAdapter timeLineAdapter;
    private TimeLinePresenter presenter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        initUI();
        initAdapter();
        clickListener();
        setupPresenter();
    }



    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        timeLineAdapter = new TimeLineAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(timeLineAdapter);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, "onLoadMore: paggination");
                presenter.paggination();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.song_recycler);
        showListState();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intProgressbar();
        setTitleWithBackPress("Timeline");
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

    }



    @Override
    public void setupPresenter() {
        presenter = new TimeLinePresenter(this, this);
        presenter.onViewAttached();

    }

    @Override
    public void updateList(List<TimeLineModel> totalNewsList) {
        timeLineAdapter.updateList(totalNewsList);
        if(totalNewsList.isEmpty()){
            showEmptyState();
        }else {
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



}
