package com.chandlersystem.chandler.ui.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityMainBinding;
import com.chandlersystem.chandler.ui.adapters.FragmentAdapter;
import com.chandlersystem.chandler.ui.main.state.CreateDealRequestFragment;
import com.chandlersystem.chandler.ui.deal.DealFragment;
import com.chandlersystem.chandler.ui.main.state.MainState;
import com.chandlersystem.chandler.ui.product_search.ProductSearchActivity;
import com.chandlersystem.chandler.ui.product_search.ProductSearchFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, DealFragment.OnFragmentInteractionListener, CreateDealRequestFragment.OnCreateDealRequestInteraction {
    private static final String TAG = MainActivity.class.getCanonicalName();

    private ActivityMainBinding mBinding;

    private MainState mMainState;

    private FragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupViews();
        handleEvents();
    }

    private void handleEvents() {
        mBinding.navHome.setOnClickListener(this);
        mBinding.navRequest.setOnClickListener(this);
        mBinding.navCreate.setOnClickListener(this);
        mBinding.navNotification.setOnClickListener(this);
        mBinding.navProfile.setOnClickListener(this);
        mBinding.viewpager.addOnPageChangeListener(this);
        mBinding.toolbar.ivCart.setOnClickListener(this);
        mBinding.toolbar.ivSearch.setOnClickListener(this);
    }

    private void setupViews() {
        mMainState = new MainState(this, mBinding, 0);

        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(DealFragment.newInstance());
        fragmentList.add(DealFragment.newInstance());
        fragmentList.add(CreateDealRequestFragment.newInstance());
        fragmentList.add(DealFragment.newInstance());
        fragmentList.add(DealFragment.newInstance());
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        mBinding.viewpager.setAdapter(mFragmentAdapter);
        mBinding.viewpager.setOffscreenPageLimit(5);
        setFragmentPosition(0);
    }

    public static Intent getInstance(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        return i;
    }

    private void setFragmentPosition(int position) {
        mMainState.navigate(position);
        mBinding.viewpager.setCurrentItem(position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_home:
                setFragmentPosition(0);
                break;
            case R.id.nav_request:
                setFragmentPosition(1);
                break;
            case R.id.nav_create:
                setFragmentPosition(2);
                break;
            case R.id.nav_notification:
                setFragmentPosition(3);
                break;
            case R.id.nav_profile:
                setFragmentPosition(4);
                break;
            case R.id.iv_cart:
                break;
            case R.id.iv_search:
                openProductSearch();
                break;
        }
    }

    private void openProductSearch() {
        Intent i = ProductSearchActivity.getIntent(this);
        startActivity(i);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setFragmentPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCreateRequest() {

    }

    @Override
    public void onCreateDeal() {

    }
}
