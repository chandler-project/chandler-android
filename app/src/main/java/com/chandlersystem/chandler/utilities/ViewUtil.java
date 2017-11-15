package com.chandlersystem.chandler.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chandlersystem.chandler.GlideApp;
import com.chandlersystem.chandler.R;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ViewUtil {

    private ViewUtil() {
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static void setText(TextView textView, String text) {
        boolean isStringValidated = ValidateUtil.checkString(text);
        ViewUtil.toggleView(textView, isStringValidated);
        if (isStringValidated) {
            textView.setText(text);
        }
    }

    public static void setTextWithHtml(TextView textView, String text) {
        boolean isStringValidated = ValidateUtil.checkString(text);
        ViewUtil.toggleView(textView, isStringValidated);
        if (isStringValidated) {
            textView.setText(Html.fromHtml(text));
        }
    }

    public static Spannable getColoredString(Context context, CharSequence text, int color) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new ForegroundColorSpan(color), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
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
     * Return a bitmap from URL
     *
     * @param imageUrl
     * @return
     */
    public static Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Return a bitmap from URL by using Glide
     *
     * @param imageUrl
     * @return
     */
    public static Bitmap getBitmapFromUrl(Context context, String imageUrl) {
        try {
            return GlideApp.
                    with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Show image without callback
     *
     * @param context
     * @param url
     * @param view
     */
    public static void showImage(Context context, String url, ImageView view) {
        GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable.ic_placeholder)
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

    public static Bitmap resizeBitmapIcon(Context context, int resourceId, int width, int height) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), resourceId);
        return Bitmap.createScaledBitmap(icon, width, height, false);
    }
}
