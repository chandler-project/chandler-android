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
            R.drawable.ic_home_inactive,
            R.drawable.ic_request_inactive,
            R.drawable.ic_plus_inactive,
            R.drawable.ic_notification_inactive,
            R.drawable.ic_profile_inactive
    );
    private final List<Integer> mActiveResource = Arrays.asList(
            R.drawable.ic_home_active,
            R.drawable.ic_request_active,
            R.drawable.ic_plus_active,
            R.drawable.ic_notification_active,
            R.drawable.ic_profile_active
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
                mBinding.ivNavHome,
                mBinding.ivNavRequest,
                mBinding.ivNavCreate,
                mBinding.ivNavNotification,
                mBinding.ivNavProfile
        );

        // Set up text list
        mTextViews = Arrays.asList(
                mBinding.tvNavHome,
                mBinding.tvNavRequest,
                null,
                mBinding.tvNavNotification,
                mBinding.tvNavProfile
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
            changeTextColor(mCurrentPos, pos);

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
    private void changeTextColor(int inActivePosition, int activePosition) {
        if (mTextViews == null)
            return;

        if (mTextViews.get(activePosition) != null) {
            int activeColor = ContextCompat.getColor(mContext, R.color.colorPrimary);
            mTextViews.get(activePosition).setTextColor(activeColor);
        }

        if (mTextViews.get(inActivePosition) != null) {
            int inActiveColor = ContextCompat.getColor(mContext, R.color.colorHint);
            mTextViews.get(inActivePosition).setTextColor(inActiveColor);
        }
    }
}
