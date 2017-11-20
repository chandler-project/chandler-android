package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.ui.product_detail.ProductActivityFragment;
import com.chandlersystem.chandler.ui.product_detail.ProductCommentActivity;
import com.chandlersystem.chandler.ui.product_detail.ProductInforFragment;

import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {
    private Context context;

    public TabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return ProductInforFragment.newInstance();
            case 1:
                return ProductActivityFragment.newInstance();
            case 2:
                return ProductCommentActivity.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            default:
                return context.getString(R.string.content_detail);
            case 1:
                return context.getString(R.string.content_activity);
            case 2:
                return context.getString(R.string.content_comment);
        }
    }
}
