package com.dhirain.musicgo.network;

import com.dhirain.musicgo.model.MusicModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Dhirain Jain on 14-09-2017.
 */

public interface MusicClient {
    @GET("/studio")
    Call<List<MusicModel>> getAllMusic();
}
