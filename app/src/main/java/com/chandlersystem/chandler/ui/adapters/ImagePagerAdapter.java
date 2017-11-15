package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mUrlList;

    public ImagePagerAdapter(Context context, List<String> urlList) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mUrlList = urlList;
    }

    @Override
    public int getCount() {
        return mUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View imageLayout = mLayoutInflater.inflate(R.layout.item_slide_show, container, false);
        ImageView ivSlideShow = imageLayout.findViewById(R.id.iv_slide_show);

        String url = mUrlList.get(position);
        ViewUtil.showImage(mContext, url, ivSlideShow);

        container.addView(imageLayout);
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
