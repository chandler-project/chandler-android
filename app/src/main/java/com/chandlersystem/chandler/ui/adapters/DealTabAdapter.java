package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.ui.deal_detail.DealActivityFragment;
import com.chandlersystem.chandler.ui.deal_detail.DealCommentFragment;
import com.chandlersystem.chandler.ui.deal_detail.DealInforFragment;

public class DealTabAdapter extends FragmentPagerAdapter {
    private Context context;

    public DealTabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return DealInforFragment.newInstance();
            case 1:
                return DealActivityFragment.newInstance();
            case 2:
                return DealCommentFragment.newInstance();
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
