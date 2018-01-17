package com.chandlersystem.chandler.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityUserProfileBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.feedback.FeedbackActivity;
import com.chandlersystem.chandler.ui.user_deal.UserDealActivity;
import com.chandlersystem.chandler.ui.user_request.UserRequestActivity;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = UserProfileActivity.class.getCanonicalName();

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private ActivityUserProfileBinding mBinding;

    private static final String ARGUMENT_USER_ID = "argument-user-id";

    public static Intent getIntent(Context context, String userId) {
        Intent i = new Intent(context, UserProfileActivity.class);
        i.putExtra(ARGUMENT_USER_ID, userId);
        return i;
    }

    private String mUserId;

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
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();

        mActivityComponent.inject(this);

        mUserId = getIntent().getStringExtra(ARGUMENT_USER_ID);

        setupToolbar();
        customerInfoApi();
        handleEvents();
    }

    private void customerInfoApi() {
        mCompositeDisposable.add(mApi.getPublicProfile(mUserId)
                .compose(RxUtil.withSchedulers())
                .subscribe(userRetrofitResponseItem -> showCustomerInfo(userRetrofitResponseItem.item), Throwable::printStackTrace));
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText(getString(R.string.content_user_profile));
    }

    private void showCustomerInfo(User user) {
        ViewUtil.setText(mBinding.tvName, user.getFullName());
        ViewUtil.showImage(this, ApiConstant.BASE_URL_VER1 + user.getAvatar(), mBinding.ivProfile);
        mBinding.description.setText(user.getBio());
        ViewUtil.setText(mBinding.btnPoint, user.getPoints() + " " + (user.getPoints() > 2 ? getString(R.string.content_points) : getString(R.string.content_point)));
    }

    private void handleEvents() {
        mCompositeDisposable.add(yourDeal().subscribe(o -> startUserDealActivity(), Throwable::printStackTrace));

        mCompositeDisposable.add(yourRequest().subscribe(o -> startUserRequestActivity(), Throwable::printStackTrace));

        mCompositeDisposable.add(yourFeedback().subscribe(o -> startFeedbackActivity(), Throwable::printStackTrace));

        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));
    }

    private void startFeedbackActivity() {
        startActivity(FeedbackActivity.getInstance(this));
    }

    private Observable<Object> yourFeedback() {
        return RxView.clicks(mBinding.layoutFeedback);
    }

    private void startUserRequestActivity() {
        startActivity(UserRequestActivity.getIntent(this, UserManager.getUserSync().getId()));
    }

    private Observable<Object> yourRequest() {
        return RxView.clicks(mBinding.layoutYourRequest);
    }

    private void startUserDealActivity() {
        startActivity(UserDealActivity.getInstance(this, UserManager.getUserSync().getId()));
    }

    private Observable<Object> yourDeal() {
        return RxView.clicks(mBinding.layoutYourHotDeal);
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }
}
