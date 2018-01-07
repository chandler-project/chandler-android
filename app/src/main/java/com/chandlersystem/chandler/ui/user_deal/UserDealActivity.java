package com.chandlersystem.chandler.ui.user_deal;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.custom_views.CategoryDecoration;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityCategoryDetailBinding;
import com.chandlersystem.chandler.databinding.ActivityUserDealBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.DealAdapter;
import com.chandlersystem.chandler.ui.deal.CategoryDetailActivity;
import com.chandlersystem.chandler.ui.deal_detail.DealDetailActivity;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class UserDealActivity extends AppCompatActivity {
    private ActivityUserDealBinding mBinding;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private DealAdapter mDealAdapter;

    public static Intent getInstance(Context context) {
        return new Intent(context, UserDealActivity.class);
    }

    @Inject
    ChandlerApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_deal);

        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();

        mActivityComponent.inject(this);

        setupViews();
        handleEvents();
        callApiGetUserDeal();
    }

    private void callApiGetUserDeal() {
        mCompositeDisposable.add(
                mApi.getDealList(UserManager.getUserSync().getAuthorization())
                        .compose(RxUtil.withSchedulers())
                        .compose(RxUtil.withProgressBar(mBinding.layoutProgressBar.progressBar))
                        .map(dealRetrofitResponseListItem -> dealRetrofitResponseListItem.items)
                        .subscribe(this::setAdapter, throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }

    private void setupViews() {
        setupToolbar();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerViewDeals.setLayoutManager(gridLayoutManager);
        mBinding.recyclerViewDeals.setNestedScrollingEnabled(true);
        mBinding.recyclerViewDeals.setHasFixedSize(true);
    }

    private void setAdapter(List<Deal> dealList) {
        mDealAdapter = new DealAdapter(this, dealList, DealAdapter.DealType.DEAL_MAIN);
        mBinding.recyclerViewDeals.setAdapter(mDealAdapter);

        dealClicks();
    }

    private void dealClicks() {
        mCompositeDisposable.add(mDealAdapter.getDealClicks()
                .subscribe(this::startDealDetailActivity, Throwable::printStackTrace));
    }

    private void startDealDetailActivity(Deal deal) {
        startActivity(DealDetailActivity.getInstance(this, deal));
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText(getString(R.string.content_your_hot_deal));
    }
}
