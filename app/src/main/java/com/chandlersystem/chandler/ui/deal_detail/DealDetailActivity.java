package com.chandlersystem.chandler.ui.deal_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.RxBus;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.events.BuyDealUpdate;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityDealDetailBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.DealTabAdapter;
import com.chandlersystem.chandler.ui.dialogs.BuyDialog;
import com.chandlersystem.chandler.ui.dialogs.BuyDialog.BuyDialogCallback;
import com.chandlersystem.chandler.ui.profile.UserProfileActivity;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class DealDetailActivity extends AppCompatActivity implements BuyDialogCallback {
    private ActivityDealDetailBinding mBinding;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private static final String ARGUMENT_DEAL = "argument-deal";

    private Deal mDeal;

    @Inject
    ChandlerApi mApi;

    public static Intent getInstance(Context context, Deal deal) {
        Intent i = new Intent(context, DealDetailActivity.class);
        i.putExtra(ARGUMENT_DEAL, deal);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();

        mActivityComponent.inject(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_deal_detail);

        mDeal = getIntent().getParcelableExtra(ARGUMENT_DEAL);
        if (mDeal.getShipper().getId().equals(UserManager.getUserSync().getId())) {
            ViewUtil.toggleView(mBinding.btnBuy, false);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mBinding.viewpagerProduct.getLayoutParams();
            marginLayoutParams.bottomMargin = 0;
            mBinding.viewpagerProduct.setLayoutParams(marginLayoutParams);
        }

        init();
    }

    private void init() {
        setupToolbar();
        setupViewPagerAndTabLayout();
        handleEvents();
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));

        mCompositeDisposable.add(buttonBuyClicks()
                .subscribe(o -> DialogUtil.showBuyDialog(this), Throwable::printStackTrace));
    }

    private void buyDeal(int amount) {
        mCompositeDisposable.add(mApi.buyDeal(mDeal.getId(), UserManager.getUserSync().getAuthorization(), amount)
                .compose(RxUtil.withSchedulers())
                .map(dealRetrofitResponseItem -> dealRetrofitResponseItem.item)
                .subscribe(this::buySuccess, this::buyError));
    }

    private void buySuccess(Deal deal) {
        Toast.makeText(this, getString(R.string.content_buy_success), Toast.LENGTH_SHORT).show();
        mDeal = deal;
        mBinding.viewpagerProduct.setCurrentItem(1);
        RxBus.getInstance().post(new BuyDealUpdate(deal.getRequesters()));
    }

    private void buyError(Throwable throwable) {
        Toast.makeText(this, getString(R.string.content_buy_error), Toast.LENGTH_SHORT).show();
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }

    private Observable<Object> buttonBuyClicks() {
        return RxView.clicks(mBinding.btnBuy);
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText(mDeal.getProductName());
        mBinding.layoutToolbar.tvSubTitle.setText(mDeal.getRequesters().size() + " " + getString(R.string.content_requester));
    }

    private void setupViewPagerAndTabLayout() {
        DealTabAdapter adapter = new DealTabAdapter(getSupportFragmentManager(), this, mDeal);
        mBinding.viewpagerProduct.setAdapter(adapter);
        mBinding.viewpagerProduct.setOffscreenPageLimit(3);

        mBinding.tabProduct.setupWithViewPager(mBinding.viewpagerProduct);
    }

    @Override
    public void onBuyDialogResponse(int amount) {
        buyDeal(amount);
    }
}
