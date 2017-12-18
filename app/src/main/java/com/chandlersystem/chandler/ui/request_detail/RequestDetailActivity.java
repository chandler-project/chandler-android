package com.chandlersystem.chandler.ui.request_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityRequestDetailBinding;
import com.chandlersystem.chandler.ui.adapters.DealTabAdapter;
import com.chandlersystem.chandler.ui.adapters.RequestTabAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class RequestDetailActivity extends AppCompatActivity {
    private ActivityRequestDetailBinding mBinding;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static Intent getInstance(Context context) {
        return new Intent(context, RequestDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_detail);
        setupToolbar();
        setupViewPagerAndTabLayout();
        handleEvents();
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));

        mCompositeDisposable.add(buttonBetClicks()
                .subscribe(o -> {
                    Toast.makeText(this, "User bought bet it! Huraaaaaaaaa", Toast.LENGTH_SHORT).show();
                }, Throwable::printStackTrace));
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }

    private Observable<Object> buttonBetClicks() {
        return RxView.clicks(mBinding.btnBet);
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText("MDR 1000 (Black version)");
        mBinding.layoutToolbar.tvSubTitle.setText("1000 request");
        mBinding.layoutToolbar.ivCart.setVisibility(View.GONE);
    }

    private void setupViewPagerAndTabLayout() {
        RequestTabAdapter adapter = new RequestTabAdapter(getSupportFragmentManager(), this);
        mBinding.viewpagerRequest.setAdapter(adapter);
        mBinding.viewpagerRequest.setOffscreenPageLimit(2);

        mBinding.tabRequest.setupWithViewPager(mBinding.viewpagerRequest);
    }
}
