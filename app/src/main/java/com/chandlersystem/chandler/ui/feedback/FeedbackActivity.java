package com.chandlersystem.chandler.ui.feedback;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.Feedback;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityFeedbackBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.FeedbackAdapter;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class FeedbackActivity extends AppCompatActivity {
    private ActivityFeedbackBinding mBinding;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private FeedbackAdapter mFeedbackAdapter;

    public static Intent getInstance(Context context) {
        return new Intent(context, FeedbackActivity.class);
    }

    @Inject
    ChandlerApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);

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
                mApi.getFeedback(UserManager.getUserSync().getId())
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
        mBinding.recyclerViewFeedback.setLayoutManager(gridLayoutManager);
        mBinding.recyclerViewFeedback.setNestedScrollingEnabled(true);
        mBinding.recyclerViewFeedback.setHasFixedSize(true);
        mBinding.recyclerViewFeedback.setEmptyView(mBinding.layoutEmpty.layoutEmpty);
        mBinding.layoutEmpty.tvEmpty.setText(getText(R.string.content_there_is_no_feedback));
        ViewUtil.setImage(mBinding.layoutEmpty.ivEmpty, R.drawable.ic_empty_aerostat);
    }

    private void setAdapter(List<Feedback> feedbacks) {
        mFeedbackAdapter = new FeedbackAdapter(feedbacks, this);
        mBinding.recyclerViewFeedback.setAdapter(mFeedbackAdapter);
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText(getString(R.string.content_your_feedback));
    }
}
