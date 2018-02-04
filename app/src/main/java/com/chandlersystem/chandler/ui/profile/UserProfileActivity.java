package com.chandlersystem.chandler.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.Feedback;
import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.data.models.request.FeedbackBody;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityUserProfileBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.FeedbackAdapter;
import com.chandlersystem.chandler.ui.feedback.FeedbackActivity;
import com.chandlersystem.chandler.ui.user_deal.UserDealActivity;
import com.chandlersystem.chandler.ui.user_request.UserRequestActivity;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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

    private FeedbackAdapter mFeedbackAdapter;

    private int mFeedbackCount;

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

        if (mUserId.equals(UserManager.getUserSync().getId())) {
            finish();
        }

        setupView();
        callApi();
        handleEvents();
    }

    private void setupView() {
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

    private void callApi() {
        customerInfoApi();
        getFeedbackApi();
    }

    private void getFeedbackApi() {
        mCompositeDisposable.add(mApi.getFeedback(mUserId)
                .compose(RxUtil.withSchedulers())
                .subscribe(feedbackRetrofitResponseListItem -> {
                    setAdapter(feedbackRetrofitResponseListItem.items);
                    setFeedbackAmount(feedbackRetrofitResponseListItem.items.size());
                }, throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void setFeedbackAmount(int size) {
        mBinding.tvFeedback.setText(getString(R.string.content_feedback) + " (" + size + ")");
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
        ViewUtil.setText(mBinding.tvDescription, user.getBio());
        ViewUtil.setText(mBinding.tvAddress, user.getAddress());
        ViewUtil.setText(mBinding.tvPhoneNumber, user.getPhoneNumber());
        ViewUtil.setText(mBinding.btnPoint, user.getPoints() + " " + (user.getPoints() > 2 ? getString(R.string.content_points) : getString(R.string.content_point)));
    }

    private void handleEvents() {
        mCompositeDisposable.add(yourDeal().subscribe(o -> startUserDealActivity(), Throwable::printStackTrace));

        mCompositeDisposable.add(yourRequest().subscribe(o -> startUserRequestActivity(), Throwable::printStackTrace));

        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));

        mCompositeDisposable.add(buttonSendFeedbackClicks().subscribe(o -> sendFeedbackApi(), Throwable::printStackTrace));

        mCompositeDisposable.add(sendFeedbackTextChanges()
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        enableButtonNext();
                    } else {
                        disableButtonNext();
                    }
                }, Throwable::printStackTrace));
    }

    private Observable<Boolean> sendFeedbackTextChanges() {
        return RxTextView.textChanges(mBinding.etFeedback)
                .map(charSequence -> {
                    int currentLength = charSequence.toString().length();
                    return currentLength > 0;
                });
    }

    private void sendFeedbackApi() {
        FeedbackBody body = new FeedbackBody();
        body.setContent(mBinding.etFeedback.getText().toString());
        mCompositeDisposable.add(mApi.sendFeedback(body, mUserId, UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .map(feedbackRetrofitResponseItem -> feedbackRetrofitResponseItem.item)
                .subscribe(feedback -> {
                    mFeedbackAdapter.addItem(feedback);
                    mBinding.etFeedback.setText("");
                    ViewUtil.hideSoftKey(this);
                    setFeedbackAmount(mFeedbackAdapter.getItemCount());
                }, throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void disableButtonNext() {
        mBinding.btnSendFeedback.setEnabled(false);
        mBinding.btnSendFeedback.setBackgroundColor(ContextCompat.getColor(this, R.color.colorInactive));
    }

    private void enableButtonNext() {
        mBinding.btnSendFeedback.setEnabled(true);
        mBinding.btnSendFeedback.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private Observable<Object> buttonSendFeedbackClicks() {
        return RxView.clicks(mBinding.btnSendFeedback);
    }

    private void startUserRequestActivity() {
        startActivity(UserRequestActivity.getIntent(this, mUserId));
    }

    private Observable<Object> yourRequest() {
        return RxView.clicks(mBinding.layoutYourRequest);
    }

    private void startUserDealActivity() {
        startActivity(UserDealActivity.getInstance(this, mUserId));
    }

    private Observable<Object> yourDeal() {
        return RxView.clicks(mBinding.layoutYourHotDeal);
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }
}
