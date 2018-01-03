package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.ui.create_request.CompleteCreateDealFragment;
import com.chandlersystem.chandler.ui.create_request.SelectCategoryFragment;
import com.chandlersystem.chandler.ui.create_request.SelectCityFragment;
import com.chandlersystem.chandler.ui.create_request.SelectDateFragment;
import com.chandlersystem.chandler.ui.create_request.SelectPriceFragment;

public class RequestPagerAdapter extends FragmentPagerAdapter {
    public static final int TOTAL_PAGE = 5;
    private Context context;

    public RequestPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SelectCategoryFragment.getInstance();
            case 1:
                return SelectDateFragment.getInstance(context.getString(R.string.content_when_do_you_need_the_product));
            case 2:
                return SelectPriceFragment.getInstance(context.getString(R.string.content_estimate_shipping_budget), SelectPriceFragment.TYPE_REQUEST);
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
