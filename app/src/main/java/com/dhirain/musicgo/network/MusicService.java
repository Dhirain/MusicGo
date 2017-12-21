package com.dhirain.musicgo.network;

import com.dhirain.musicgo.MusicGOApp;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dhirain Jain on 14-09-2017.
 */

public class MusicService {

    public static final String API_BASE_URL = "http://starlord.hackerearth.com/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static MusicClient builder;

    public static MusicClient instance() {
        return builder;
    }

    static {
        //setup cache
        httpClient.addNetworkInterceptor(new CacheInterceptor());
        httpClient.cache(setCacheSize(2 * 1024 * 1024));// 2 MB

        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(MusicClient.class);
    }

    private static Cache setCacheSize(int cacheSize) {
        File httpCacheDirectory = new File(MusicGOApp.singleton().getContext().getCacheDir(), "responses");
        return new Cache(httpCacheDirectory, cacheSize);
    }

}
