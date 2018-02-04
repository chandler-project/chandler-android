package com.chandlersystem.chandler.ui.request_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.RxBus;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.events.BidRequestUpdate;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.data.models.request.BidRequestBody;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityRequestDetailBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.DealTabAdapter;
import com.chandlersystem.chandler.ui.adapters.RequestTabAdapter;
import com.chandlersystem.chandler.ui.dialogs.BidDialog;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.math.BigInteger;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class RequestDetailActivity extends AppCompatActivity implements BidDialog.BidDialogCallback {
    private ActivityRequestDetailBinding mBinding;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private static final String ARGUMENT_REQUEST = "tag-request";
    private Request mRequest;

    public static Intent getInstance(Context context, Request request) {
        Intent i = new Intent(context, RequestDetailActivity.class);
        i.putExtra(ARGUMENT_REQUEST, request);
        return i;
    }

    @Inject
    ChandlerApi mApi;

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

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_detail);
        mRequest = getIntent().getParcelableExtra(ARGUMENT_REQUEST);

        if (mRequest.getOwner().getId().equals(UserManager.getUserSync().getId())) {
            ViewUtil.toggleView(mBinding.btnBet, false);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mBinding.viewpagerRequest.getLayoutParams();
            marginLayoutParams.bottomMargin = 0;
            mBinding.viewpagerRequest.setLayoutParams(marginLayoutParams);
        }

        init();
    }

    private void init() {
        setupToolbar();
        setupViews();
        handleEvents();
    }

    private void setupViews() {
        setupToolbar();
        setupViewPagerAndTabLayout();
        setupButtonBid();
    }

    private void setupButtonBid() {
        mBinding.btnBet.setText(getString(R.string.content_bid_now));
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));

        mCompositeDisposable.add(buttonBetClicks()
                .subscribe(o -> DialogUtil.showBidDialog(this, mRequest), Throwable::printStackTrace));
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }

    private Observable<Object> buttonBetClicks() {
        return RxView.clicks(mBinding.btnBet);
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText(mRequest.getProductName());
    }

    private void setupViewPagerAndTabLayout() {
        RequestTabAdapter adapter = new RequestTabAdapter(getSupportFragmentManager(), this, mRequest);
        mBinding.viewpagerRequest.setAdapter(adapter);
        mBinding.viewpagerRequest.setOffscreenPageLimit(2);
        mBinding.tabRequest.setupWithViewPager(mBinding.viewpagerRequest);
    }

    private void callBidApi(String requestId, BidRequestBody bidRequestBody) {
        mCompositeDisposable.add(mApi.bidRequest(bidRequestBody, requestId, UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .map(requestRetrofitResponseItem -> requestRetrofitResponseItem.item)
                .subscribe(this::bidSuccess, this::bidError));
    }

    private void bidSuccess(Request item) {
        Toast.makeText(this, getString(R.string.content_bid_sucess), Toast.LENGTH_SHORT).show();
        mRequest = item;
        RxBus.getInstance().post(new BidRequestUpdate(item.getBidders()));
        mBinding.viewpagerRequest.setCurrentItem(1);
    }

    private void bidError(Throwable throwable) {
        Toast.makeText(this, getString(R.string.content_bid_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBid(String requestId, BidRequestBody bidRequestBody) {
        callBidApi(requestId, bidRequestBody);
    }
}
