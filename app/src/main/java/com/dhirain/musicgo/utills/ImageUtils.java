package com.dhirain.musicgo.utills;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Dhirain Jain on 19-12-2017.
 */

public class ImageUtils {

    public static void setImage(Context context, String url, ImageView imageView, int stubImage) {
        Glide.with(context).load(url)
                .centerCrop()
                .placeholder(ContextCompat.getDrawable(context, stubImage))
                .error(ContextCompat.getDrawable(context, stubImage)).
                into(imageView);
    }
}
