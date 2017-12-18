package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.ui.deal_detail.DealActivityFragment;
import com.chandlersystem.chandler.ui.deal_detail.DealCommentFragment;
import com.chandlersystem.chandler.ui.deal_detail.DealInforFragment;
import com.chandlersystem.chandler.ui.request_detail.RequestActivityFragment;
import com.chandlersystem.chandler.ui.request_detail.RequestInforFragment;

public class RequestTabAdapter extends FragmentPagerAdapter {
    private Context context;

    public RequestTabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return RequestInforFragment.newInstance();
            case 1:
                return RequestActivityFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            default:
                return context.getString(R.string.content_detail);
            case 1:
                return context.getString(R.string.content_activity);
        }
    }
}
