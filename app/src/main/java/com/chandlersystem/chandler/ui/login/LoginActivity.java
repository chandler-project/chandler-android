package com.chandlersystem.chandler.ui.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.data.models.request.LoginRequest;
import com.chandlersystem.chandler.data.models.response.AuthenticationRespone;
import com.chandlersystem.chandler.data.models.response.RetrofitResponseItem;
import com.chandlersystem.chandler.database.DatabaseHelper;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityLoginBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.services.AccountKitConnector;
import com.chandlersystem.chandler.services.FacebookConnector;
import com.chandlersystem.chandler.ui.main.MainActivity;
import com.chandlersystem.chandler.ui.select_category.SelectCategoryActivity;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.facebook.AccessToken;
import com.facebook.login.LoginResult;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private ActivityLoginBinding mBinding;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private boolean mIsFirstLogin;

    @Inject
    ChandlerApi mApi;

    public static Intent getInstance(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        logoutFacebook();
        injectComponent();
        handleEvents();
    }

    private void logoutFacebook() {
        FacebookConnector.getInstance().logOut();
        AccountKitConnector.getInstance().logout();
    }

    private void handleEvents() {
        // Handle event when user tapping on Facebook login button
        mCompositeDisposable.add(RxView.clicks(mBinding.btnFacebookLogin)
                .compose(RxUtil.withLongThrottleFirst())
                .subscribe(o -> buttonFacebookClicks(),
                        Throwable::printStackTrace));

        // Handle click on Phone button
        mCompositeDisposable.add(RxView.clicks(mBinding.btnPhoneNumber)
                .compose(RxUtil.withLongThrottleFirst())
                .subscribe(o -> AccountKitConnector.getInstance().login(this),
                        Throwable::printStackTrace));
    }

    private void buttonFacebookClicks() {
        mCompositeDisposable.add(DatabaseHelper.clearTable(User.class)
                .concatMap(transaction -> FacebookConnector.getInstance().logIn(LoginActivity.this))
                .compose(RxUtil.withSchedulersIO())
                .map(LoginResult::getAccessToken)
                .concatMap(this::loginWithFacebook)
                .concatMap(this::saveUser)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtil.withProgressBar(mBinding.layoutProgressBar.progressBar))
                .subscribe(this::onSignInSuccess, this::showErrorMessage));
    }

    private Observable<RetrofitResponseItem<AuthenticationRespone>> loginWithFacebook(AccessToken accessToken) {
        String userToken = accessToken.getToken();
        String userId = accessToken.getUserId();


        LoginRequest loginRequest = new LoginRequest(userToken, userId);
        return mApi.authentication(loginRequest, 1, "user");
    }

    private Observable<RetrofitResponseItem<AuthenticationRespone>> loginWithAccountKit(com.facebook.accountkit.AccessToken accessToken) {
        String userToken = accessToken.getToken();
        String userId = accessToken.getAccountId();

        LoginRequest loginRequest = new LoginRequest(userToken, userId);
        return mApi.authentication(loginRequest, 1, "user");
    }

    private Observable<Boolean> saveUser(RetrofitResponseItem<AuthenticationRespone> userResponse) {
        // Assign flag IsFirstLogin to check if this user is login already or not
        mIsFirstLogin = userResponse.item.getIsFirstLogin();
        User newUser = userResponse.item.getUser();
        newUser.setAuthorization(userResponse.item.getId());
        newUser.setFirstLogin(mIsFirstLogin);

        /*// Subscribe this user in order to get new notification from firebase
        // Don't care the result, just call API
        mCompositeDisposable.add(mManager.subscribe(newUser.getAuthToken())
                .compose(RxUtil.withSchedulersIO())
                .subscribe(responseItem1 -> {
                }, Throwable::printStackTrace));*/

        // Store user
        return UserManager.saveUserAsync(newUser);
    }

    private void injectComponent() {
        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();
        mActivityComponent.inject(this);
    }

    private void onSignInSuccess(boolean isSaved) {
        Log.d(TAG, "onSignInSuccess: " + Thread.currentThread().getName());
        // If User Model can't be saved into database, show error message
        if (!isSaved) {
            showErrorMessage(new IOException());
            return;
        }

        // Check if user is logged in or not
        if (mIsFirstLogin) {
            startSelectCategoryActivity();
        } else {
            startMainActivity();
        }
    }

    private void showErrorMessage(Throwable throwable) {
        Log.d(TAG, "onSignInFailed: " + Thread.currentThread().getName());
        throwable.printStackTrace();
        DialogUtil.showErrorDialog(this, throwable);
    }

    private void startSelectCategoryActivity() {
        startActivity(SelectCategoryActivity.getInstance(this));
    }

    private void startMainActivity() {
        finish();
        startActivity(MainActivity.getInstance(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle callback from FacebookConnector
        if (FacebookConnector.getInstance().onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        // Handle Observable from @AccountKitConnector
        mCompositeDisposable.add(DatabaseHelper.clearTable(User.class)
                .compose(RxUtil.withSchedulersIO())
                .concatMap(transaction -> AccountKitConnector.getInstance()
                        .onActivityResult(this, requestCode, resultCode, data))
                .concatMap(this::loginWithAccountKit)
                .concatMap(this::saveUser)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtil.withProgressBar(mBinding.layoutProgressBar.progressBar))
                .subscribe(this::onSignInSuccess,
                        this::showErrorMessage));
    }

    //On Activity Destroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
        // Logout FacebookConnector, AccountKitConnector
        FacebookConnector.getInstance().logOut();
        AccountKitConnector.getInstance().logout();
    }
}
