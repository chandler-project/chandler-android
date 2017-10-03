package com.chandlersystem.chandler.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chandlersystem.chandler.GlideApp;

public class ViewUtil {

    private ViewUtil() {
    }

    /**
     * Show/hide a class which extends from View
     *
     * @param v                 View
     * @param isAvailableToShow is a boolean value represent for show or hide view
     */
    public static void toggleView(View v, boolean isAvailableToShow) {
        if (isAvailableToShow && v.getVisibility() != View.VISIBLE) {
            v.setVisibility(View.VISIBLE);
        } else if (!isAvailableToShow && v.getVisibility() == View.VISIBLE) {
            v.setVisibility(View.GONE);
        }
    }


    /**
     * Show image without callback
     *
     * @param context
     * @param url
     * @param view
     * @param placeholder
     */
    public static void showImage(Context context, String url, ImageView view, int placeholder) {
        GlideApp.with(context)
                .load(url)
                .placeholder(placeholder)
                .into(view);
    }

    /**
     * Show image asynchronous with callback
     *
     * @param context
     * @param url
     * @param view
     */
    public static void showImageWithListener(Context context, String url, ImageView view, int placeholder) {
        GlideApp.with(context)
                .load(url)
                .placeholder(placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        if (e != null) {
                            e.printStackTrace();
                        }
                        view.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        return false;
                    }
                })
                .into(view);
    }

    /**
     * Set image for @ImageView
     *
     * @param imageView
     * @param imageResId
     */
    public static void setImage(ImageView imageView, int imageResId) {
        imageView.setImageResource(imageResId);
    }

    /**
     * Hide SoftKeyboard programmatically
     *
     * @param activity
     */
    public static void hideSoftKey(Activity activity) {
        InputMethodManager imm
                = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();

        if (v == null || v.getWindowToken() == null || imm == null) {
            return;
        }

        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Transform current image into gray scale
     *
     * @param targetView
     */
    public static void grayScaleImage(ImageView targetView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        targetView.setColorFilter(filter);
    }
}
