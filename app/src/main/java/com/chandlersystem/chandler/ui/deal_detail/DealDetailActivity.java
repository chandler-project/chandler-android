package com.chandlersystem.chandler.ui.deal_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityDealDetailBinding;
import com.chandlersystem.chandler.ui.adapters.DealTabAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class DealDetailActivity extends AppCompatActivity {
    private ActivityDealDetailBinding mBinding;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static Intent getInstance(Context context) {
        return new Intent(context, DealDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_deal_detail);
        setupToolbar();
        setupViewPagerAndTabLayout();
        handleEvents();
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));

        mCompositeDisposable.add(buttonBuyClicks()
                .subscribe(o -> {
                    Toast.makeText(this, "User bought it! Huraaaaaaaaa", Toast.LENGTH_SHORT).show();
                }, Throwable::printStackTrace));
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }

    private Observable<Object> buttonBuyClicks() {
        return RxView.clicks(mBinding.btnBuy);
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText("MDR 1000 (Black version)");
        mBinding.layoutToolbar.tvSubTitle.setText("1000 buyer");
    }

    private void setupViewPagerAndTabLayout() {
        DealTabAdapter adapter = new DealTabAdapter(getSupportFragmentManager(), this);
        mBinding.viewpagerProduct.setAdapter(adapter);
        mBinding.viewpagerProduct.setOffscreenPageLimit(3);

        mBinding.tabProduct.setupWithViewPager(mBinding.viewpagerProduct);
    }
}
