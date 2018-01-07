package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.ui.request_detail.BidFragment;
import com.chandlersystem.chandler.ui.request_detail.RequestInforFragment;

public class RequestTabAdapter extends FragmentPagerAdapter {
    private Context context;
    private Request request;

    public RequestTabAdapter(FragmentManager fm, Context context, Request request) {
        super(fm);
        this.context = context;
        this.request = request;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return RequestInforFragment.newInstance(request);
            case 1:
                return BidFragment.newInstance();
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
                return context.getString(R.string.content_bid);
        }
    }
}
