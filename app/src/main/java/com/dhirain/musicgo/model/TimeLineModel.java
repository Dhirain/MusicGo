package com.dhirain.musicgo.model;

/**
 * Created by Dhirain Jain on 21-12-2017.
 */

public class TimeLineModel {

    public TimeLineModel(String song, String artists, String coverImage, long time) {
        this.song = song;
        this.artists = artists;
        this.coverImage = coverImage;
        this.time = time;
    }

    public TimeLineModel() {
    }

    private Long _id;//for cupboard

    private String song;

    private String artists;

    private String coverImage;

    private long time;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public String toString() {
        return "TimeLineModel{" +
                "_id=" + _id +
                ", song='" + song + '\'' +
                ", artists='" + artists + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", time=" + time +
                '}';
    }
}
