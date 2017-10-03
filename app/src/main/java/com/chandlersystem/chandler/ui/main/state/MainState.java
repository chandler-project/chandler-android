package com.chandlersystem.chandler.ui.main.state;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.List;

public class MainState {
    private static final String TAG = MainState.class.getSimpleName();

    private Context mContext;

    // List ImageViews, TextView , inactive resources , active resource
    private final List<ImageView> mImageViews;
    private final List<TextView> mTextViews;

    private final List<Integer> mInActiveResource = Arrays.asList(
            R.drawable.ic_home,
            R.drawable.ic_home,
            R.drawable.ic_home,
            R.drawable.ic_home,
            R.drawable.ic_home
    );
    private final List<Integer> mActiveResource = Arrays.asList(
            R.drawable.ic_home,
            R.drawable.ic_home,
            R.drawable.ic_home,
            R.drawable.ic_home,
            R.drawable.ic_home
    );

    // View holds all image views
    private ActivityMainBinding mBinding;

    // CurrentPosition
    private int mCurrentPos;

    public MainState(Context context, ActivityMainBinding binding, int defaultPosition) {
        this.mBinding = binding;
        this.mCurrentPos = defaultPosition;
        this.mContext = context;

        // Set up image views list
        mImageViews = Arrays.asList(
                mBinding.ivNavProfile,
                mBinding.ivNavQuest,
                mBinding.ivNavCenter,
                mBinding.ivNavVoucher,
                mBinding.ivNavOther
        );

        // Set up text list
        mTextViews = Arrays.asList(
                mBinding.tvNavProfile,
                mBinding.tvNavQuest,
                null,
                mBinding.tvNavVoucher,
                mBinding.tvNavOther
        );

        navigate(defaultPosition);
    }

    /**
     * Navigate to new page
     *
     * @param pos represent the new position which state will navigate to
     */
    public void navigate(int pos) {
        if (pos != mCurrentPos) {
            // Change Image
            changeImage(mCurrentPos, pos);

            // Change text color
            changeColor(mCurrentPos, pos);

            // swap position
            mCurrentPos = pos;
        }
    }

    /**
     * Change image at new position as ACTIVE IMAGE RESOURCE
     * Replace the old one with IN ACTIVE IMAGE RESOURCE
     */
    private void changeImage(int inActivePosition, int activePosition) {
        if (mActiveResource.get(activePosition) != -1) {
            Drawable activeDrawable = ContextCompat.getDrawable(mContext, mActiveResource.get(activePosition));
            mImageViews.get(activePosition).setImageDrawable(activeDrawable);
        }

        if (mInActiveResource.get(inActivePosition) != -1) {
            Drawable inActiveDrawable = ContextCompat.getDrawable(mContext, mInActiveResource.get(inActivePosition));
            mImageViews.get(inActivePosition).setImageDrawable(inActiveDrawable);
        }
    }

    /**
     * Change color of active position => GREEN
     * Replace the old one with GRAY
     */
    private void changeColor(int inActivePosition, int activePosition) {
        if (mTextViews.get(activePosition) != null) {
            int activeColor = ContextCompat.getColor(mContext, R.color.colorGreen);
            mTextViews.get(activePosition).setTextColor(activeColor);
        }

        if (mTextViews.get(inActivePosition) != null) {
            int inActiveColor = ContextCompat.getColor(mContext, R.color.colorInactive);
            mTextViews.get(inActivePosition).setTextColor(inActiveColor);
        }
    }
}
