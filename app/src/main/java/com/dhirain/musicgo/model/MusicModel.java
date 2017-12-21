package com.dhirain.musicgo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dhirain.musicgo.database.manager.DBManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dhirain Jain on 19-12-2017.
 */

public class MusicModel implements Parcelable {
    private Long _id;//for cupboard

    @SerializedName("song")
    @Expose
    private String song;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("artists")
    @Expose
    private String artists;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;

    private boolean isFavorite = false;

    private boolean isDownloaded = false;

    public MusicModel() {
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        try {
            isFavorite = favorite;
            DBManager.instance().updateFavToItem(_id, favorite);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        try {
            isDownloaded = downloaded;
            DBManager.instance().updateDownloadToItem(_id, downloaded);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected MusicModel(Parcel in) {
        _id = in.readByte() == 0x00 ? null : in.readLong();
        song = in.readString();
        url = in.readString();
        artists = in.readString();
        coverImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(_id);
        }
        dest.writeString(song);
        dest.writeString(url);
        dest.writeString(artists);
        dest.writeString(coverImage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MusicModel> CREATOR = new Parcelable.Creator<MusicModel>() {
        @Override
        public MusicModel createFromParcel(Parcel in) {
            return new MusicModel(in);
        }

        @Override
        public MusicModel[] newArray(int size) {
            return new MusicModel[size];
        }
    };

    @Override
    public String toString() {
        return "MusicModel{" +
                "_id=" + _id +
                ", song='" + song + '\'' +
                ", url='" + url + '\'' +
                ", artists='" + artists + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", isFavorite=" + isFavorite +
                ", isDownloaded=" + isDownloaded +
                '}';
    }
}