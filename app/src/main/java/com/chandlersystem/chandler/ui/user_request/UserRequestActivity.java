package com.chandlersystem.chandler.ui.user_request;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.custom_views.LinearItemDecoration;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.FragmentRequestListBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.request_detail.RequestDetailActivity;
import com.chandlersystem.chandler.ui.requests.RequestAdapter;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class UserRequestActivity extends AppCompatActivity {
    private RequestAdapter mAdapter;

    private FragmentRequestListBinding mBinding;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    ChandlerApi mApi;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_request_list);

        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();

        mActivityComponent.inject(this);


        setupViews();
        callApiGetRequest();
        handleEvents();
    }

    private void setupViews() {
        mBinding.list.setLayoutManager(new LinearLayoutManager(this));
        mBinding.list.addItemDecoration(new LinearItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_normal)));
        mBinding.layoutToolbar.layoutToolbar.setVisibility(View.VISIBLE);
        mBinding.layoutToolbar.tvTitle.setText(getText(R.string.content_your_request));
    }

    private void handleEvents() {
        mCompositeDisposable.add(RxSwipeRefreshLayout.refreshes(mBinding.layoutSwipe)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    mBinding.layoutSwipe.setRefreshing(false);
                    callApiGetRequest();
                }, Throwable::printStackTrace));

        mBinding.layoutToolbar.ivBack.setOnClickListener(view -> finish());
    }

    private void callApiGetRequest() {
        mCompositeDisposable.add(
                mApi.getRequestList(UserManager.getUserSync().getAuthorization())
                        .compose(RxUtil.withSchedulers())
                        .subscribe(retrofitResponseListItem -> setAdapter(retrofitResponseListItem.items),
                                throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void setAdapter(List<Request> items) {
        mAdapter = new RequestAdapter(items, this);
        mBinding.list.setAdapter(mAdapter);

        requestClicks();
    }

    private void requestClicks() {
        mCompositeDisposable.add(mAdapter.getRequestClicks()
                .subscribe(this::startRequestDetailActivity, Throwable::printStackTrace));
    }

    private void startRequestDetailActivity(Request request) {
        Intent intent = RequestDetailActivity.getInstance(this, request);
        startActivity(intent);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, UserRequestActivity.class);
    }
}
