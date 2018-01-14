package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.ui.deal_detail.DealActivityFragment;
import com.chandlersystem.chandler.ui.deal_detail.DealCommentFragment;
import com.chandlersystem.chandler.ui.deal_detail.DealInforFragment;

public class DealTabAdapter extends FragmentPagerAdapter {
    private Context context;
    private Deal deal;

    public DealTabAdapter(FragmentManager fm, Context context, Deal mDeal) {
        super(fm);
        this.context = context;
        this.deal = mDeal;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return DealInforFragment.newInstance(deal);
            case 1:
                return DealActivityFragment.newInstance(deal);
            case 2:
                return DealCommentFragment.newInstance(deal);
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
                return context.getString(R.string.content_requester);
            case 2:
                return context.getString(R.string.content_comment);
        }
    }
}
