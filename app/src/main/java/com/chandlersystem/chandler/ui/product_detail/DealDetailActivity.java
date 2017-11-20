package com.chandlersystem.chandler.ui.product_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityProductDetailBinding;
import com.chandlersystem.chandler.ui.adapters.TabAdapter;

public class DealDetailActivity extends AppCompatActivity {
    private ActivityProductDetailBinding mBinding;

    public static Intent getInstance(Context context) {
        return new Intent(context, DealDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        setupViewPagerAndTabLayout();
    }

    private void setupViewPagerAndTabLayout() {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), this);
        mBinding.viewpagerProduct.setAdapter(adapter);
        mBinding.viewpagerProduct.setOffscreenPageLimit(3);

        mBinding.tabProduct.setupWithViewPager(mBinding.viewpagerProduct);
    }

}
