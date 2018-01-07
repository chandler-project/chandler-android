package com.chandlersystem.chandler.ui.deal_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityDealDetailBinding;
import com.chandlersystem.chandler.ui.adapters.DealTabAdapter;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class DealDetailActivity extends AppCompatActivity {
    private ActivityDealDetailBinding mBinding;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private static final String ARGUMENT_DEAL = "argument-deal";

    private Deal mDeal;

    public static Intent getInstance(Context context, Deal deal) {
        Intent i = new Intent(context, DealDetailActivity.class);
        i.putExtra(ARGUMENT_DEAL, deal);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_deal_detail);

        mDeal = getIntent().getParcelableExtra(ARGUMENT_DEAL);
        if (mDeal.getShipper().getId().equals(UserManager.getUserSync().getId())) {
            ViewUtil.toggleView(mBinding.btnBuy, false);
        }

        setupToolbar(mDeal);
        setupViewPagerAndTabLayout(mDeal);
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

    private void setupToolbar(Deal mDeal) {
        mBinding.layoutToolbar.tvTitle.setText(mDeal.getProductName());
        mBinding.layoutToolbar.tvSubTitle.setText(mDeal.getRequesters().size() + " " + getString(R.string.content_requester));
    }

    private void setupViewPagerAndTabLayout(Deal mDeal) {
        DealTabAdapter adapter = new DealTabAdapter(getSupportFragmentManager(), this, mDeal);
        mBinding.viewpagerProduct.setAdapter(adapter);
        mBinding.viewpagerProduct.setOffscreenPageLimit(3);

        mBinding.tabProduct.setupWithViewPager(mBinding.viewpagerProduct);
    }
}
