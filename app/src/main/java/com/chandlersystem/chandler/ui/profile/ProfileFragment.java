package com.chandlersystem.chandler.ui.profile;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chandlersystem.chandler.GlideApp;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.database.DatabaseHelper;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.database.UserObservation;
import com.chandlersystem.chandler.databinding.FragmentProfileBinding;
import com.chandlersystem.chandler.ui.become_shipper.BecomeShipperActivity;
import com.chandlersystem.chandler.ui.feedback.FeedbackActivity;
import com.chandlersystem.chandler.ui.login.LoginActivity;
import com.chandlersystem.chandler.ui.main.MainActivity;
import com.chandlersystem.chandler.ui.user_deal.UserDealActivity;
import com.chandlersystem.chandler.ui.user_request.UserRequestActivity;
import com.chandlersystem.chandler.utilities.CircleTransform;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getCanonicalName();

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private FragmentProfileBinding mBinding;

    private final UserObservation mUserObservation = new UserObservation();

    @NonNull
    public static Fragment newInstance() {
        return new ProfileFragment();
    }

    @Inject
    ChandlerApi mApi;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.getActivityComponent().inject(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        /*Fragment reviewsFragment = ReviewsFragment.newInstance(1);
        getChildFragmentManager().beginTransaction().add(R.id.review_list_container, reviewsFragment).commit();*/
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userObservation();
        handleEvents();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCompositeDisposable.clear();
        mUserObservation.unregister();
    }

    private void userObservation() {
        mCompositeDisposable.add(mUserObservation.register()
                .compose(RxUtil.withSchedulers())
                .subscribe(this::showCustomerInfor, Throwable::printStackTrace));
    }

    private void showCustomerInfor(User user) {
        ViewUtil.setText(mBinding.layoutContent.tvName, user.getFullName());
        ViewUtil.showImage(getContext(), ApiConstant.BASE_URL_VER1 + user.getAvatar(), mBinding.layoutContent.ivProfile);
        mBinding.layoutContent.description.setText(user.getBio());
        ViewUtil.setText(mBinding.layoutContent.btnPoint, user.getPoints() + " " + (user.getPoints() > 2 ? getString(R.string.content_points) : getString(R.string.content_point)));
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonEditClicks()
                .subscribe(o -> startEditProfileActivity(), Throwable::printStackTrace));

        mCompositeDisposable.add(signOut().subscribe(o -> showLogoutDialog(), Throwable::printStackTrace));

        mCompositeDisposable.add(becomeShipper()
                .subscribe(o -> {
                    if (UserManager.getUserSync().isShipper()) {
                        Toast.makeText(getContext(), getString(R.string.content_you_already_shipper), Toast.LENGTH_SHORT).show();
                    } else {
                        startBecomeShipperActivity();
                    }

                }, Throwable::printStackTrace));

        mCompositeDisposable.add(yourDeal().subscribe(o -> startUserDealActivity(), Throwable::printStackTrace));

        mCompositeDisposable.add(yourRequest().subscribe(o -> startUserRequestActivity(), Throwable::printStackTrace));

        mCompositeDisposable.add(yourFeedback().subscribe(o -> startFeedbackActivity(), Throwable::printStackTrace));
    }

    private void startFeedbackActivity() {
        startActivity(FeedbackActivity.getInstance(getContext()));
    }

    private Observable<Object> yourFeedback() {
        return RxView.clicks(mBinding.layoutContent.layoutFeedback);
    }

    private void startUserRequestActivity() {
        startActivity(UserRequestActivity.getIntent(getContext(), UserManager.getUserSync().getId()));
    }

    private Observable<Object> yourRequest() {
        return RxView.clicks(mBinding.layoutContent.layoutYourRequest);
    }

    private void startUserDealActivity() {
        startActivity(UserDealActivity.getInstance(getContext(), UserManager.getUserSync().getId()));
    }

    private Observable<Object> yourDeal() {
        return RxView.clicks(mBinding.layoutContent.layoutYourHotDeal);
    }

    private void startBecomeShipperActivity() {
        startActivity(BecomeShipperActivity.getIntent(getContext()));
    }

    private Observable<Object> becomeShipper() {
        return RxView.clicks(mBinding.layoutContent.layoutBecomeShipper);
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.content_logout_title))
                .setMessage(getString(R.string.content_logout_content))
                .setPositiveButton(getString(R.string.content_yes), (dialogInterface, i) -> {
                    logout();
                })
                .setNegativeButton(getString(R.string.content_no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create();
        builder.show();
    }

    private void logout() {
        callLogoutApi();
        mCompositeDisposable.add(DatabaseHelper.clearDatabase(getContext())
                .doOnSubscribe(disposable -> {
                    AccountKit.logOut();
                    LoginManager.getInstance().logOut();
                    DatabaseHelper.initDatabase(getContext());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transaction -> {
                            LogUtil.logD(TAG, "Clear database successfully");
                            startLoginActivity();
                        },
                        throwable -> {
                            LogUtil.logD(TAG, "Clear database failed");
                            startLoginActivity();
                        }));
    }

    private void callLogoutApi() {
        mCompositeDisposable.add(mApi.logout(UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(retrofitResponseItem -> {
                }, Throwable::printStackTrace));
    }

    private void startLoginActivity() {
        getActivity().finish();
        startActivity(LoginActivity.getInstance(getContext()));
    }

    private Observable<Object> signOut() {
        return RxView.clicks(mBinding.layoutContent.tvSignOut);
    }

    private void startEditProfileActivity() {
        startActivity(EditProfileActivity.getInstance(getContext()));
    }

    private Observable<Object> buttonEditClicks() {
        return RxView.clicks(mBinding.layoutContent.layoutEditProfile);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
