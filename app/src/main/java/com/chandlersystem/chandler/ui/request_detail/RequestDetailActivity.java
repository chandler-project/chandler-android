package com.chandlersystem.chandler.ui.request_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityRequestDetailBinding;
import com.chandlersystem.chandler.ui.adapters.DealTabAdapter;
import com.chandlersystem.chandler.ui.adapters.RequestTabAdapter;
import com.chandlersystem.chandler.ui.dialogs.BidDialog;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.math.BigInteger;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class RequestDetailActivity extends AppCompatActivity implements BidDialog.OnAlertDialogInteraction {
    private ActivityRequestDetailBinding mBinding;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private static final String ARGUMENT_REQUEST = "tag-request";
    private Request mRequest;

    public static Intent getInstance(Context context, Request request) {
        Intent i = new Intent(context, RequestDetailActivity.class);
        i.putExtra(ARGUMENT_REQUEST, request);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_detail);
        mRequest = getIntent().getParcelableExtra(ARGUMENT_REQUEST);
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
        /*if (mRequest.getOwner().getId().equals(UserManager.getUserSync().getId())) {
            ViewUtil.toggleView(mBinding.btnBet, false);
        }*/
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));

        mCompositeDisposable.add(buttonBetClicks()
                .subscribe(o -> {
                    DialogUtil.showBidDialog(this, mRequest.getAmount());
                }, Throwable::printStackTrace));
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }

    private Observable<Object> buttonBetClicks() {
        return RxView.clicks(mBinding.btnBet);
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText(mRequest.getProductName());
        //ViewUtil.setText(mBinding.layoutToolbar.tvSubTitle, request.getBidder);
        mBinding.layoutToolbar.ivCart.setVisibility(View.GONE);
    }

    private void setupViewPagerAndTabLayout() {
        RequestTabAdapter adapter = new RequestTabAdapter(getSupportFragmentManager(), this, mRequest);
        mBinding.viewpagerRequest.setAdapter(adapter);
        mBinding.viewpagerRequest.setOffscreenPageLimit(2);

        mBinding.tabRequest.setupWithViewPager(mBinding.viewpagerRequest);
    }

    @Override
    public void onAlertDialogDismiss() {

    }
}
