package com.chandlersystem.chandler.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chandlersystem.chandler.ui.create_request.CompleteCreateDealFragment;
import com.chandlersystem.chandler.ui.create_request.SelectCategoryFragment;
import com.chandlersystem.chandler.ui.create_request.SelectCityFragment;
import com.chandlersystem.chandler.ui.create_request.SelectDateFragment;
import com.chandlersystem.chandler.ui.create_request.SelectPriceFragment;

public class RequestPagerAdapter extends FragmentPagerAdapter {
    public static final int TOTAL_PAGE = 5;

    public RequestPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SelectCategoryFragment.getInstance();
            case 1:
                return SelectDateFragment.getInstance();
            case 2:
                return SelectPriceFragment.getInstance();
            case 3:
                return SelectCityFragment.getInstance();
            case 4:
                return CompleteCreateDealFragment.getInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TOTAL_PAGE;
    }
}