package com.chandlersystem.chandler.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chandlersystem.chandler.di.scopes.ActivityContext;
import com.chandlersystem.chandler.services.FacebookConnector;
import com.facebook.accountkit.AccessToken;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
public class LoginPresenterImpl implements LoginPresenter {
    private static final String TAG = LoginPresenterImpl.class.getSimpleName();

    private final LoginView mView;
    private final LoginManager mManager;

    private boolean mIsFirstLogin;

    // A subscription that keep all observable
    // Clear all observable when activity is destroyed
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    @ActivityContext
    Context mContext;

    @Inject
    LoginPresenterImpl(LoginView mView, LoginManager mManager) {
        this.mView = mView;
        this.mManager = mManager;
    }

    @Override
    public void onDetach() {
        // Dispose all disposables
        mCompositeDisposable.clear();
        mCompositeDisposable.dispose();
    }

    @Override
    public void onActivityLaunched() {
        mView.bindView();
        /*remoteConfig();
        handleEvents();*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle callback from FacebookConnector
        if (FacebookConnector.getInstance().onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        // Handle Observable from @AccountKitConnector
        /*mCompositeDisposable.add(DatabaseManager.clearTable(User.class)
                .compose(RxUtil.withSchedulersIO())
                .concatMap(transaction -> AccountKitConnector.getInstance()
                        .onActivityResult((Activity) mContext, requestCode, resultCode, data))
                .map(AccessToken::getToken)
                .concatMap(mManager::loginWithAccountKit)
                .concatMap(this::saveUser)
                .compose(withLoadingView(TRACKING_LOGIN_WITH_PHONE))
                .subscribe(LoginPresenterImpl.this::onSignInSuccess,
                        LoginPresenterImpl.this::showErrorMessage));*/

    }

    /*private void remoteConfig() {
        *//*mCompositeDisposable.add(FirebaseManager.remoteConfig((Activity) mContext)
                .compose(RxUtil.withSchedulers())
                .subscribe(s -> {
                    DialogUtil.showForceUpdateDialog(mContext, s);
                }, Throwable::printStackTrace));*//*
    }

    public void handleEvents() {
        // Handle event when user tapping on Facebook login button
        *//*mCompositeDisposable.add(RxView.clicks(mView.getFacebookLoginButton())
                .compose(RxUtil.withLongThrottleFirst())
                .subscribe(o -> mCompositeDisposable.add(DatabaseManager.clearTable(User.class)
                                .concatMap(transaction -> FacebookConnector.getInstance().logIn(mContext))
                                .compose(RxUtil.withSchedulersIO())
                                .map(loginResult -> loginResult.getAccessToken().getToken())
                                .concatMap(mManager::loginWithFacebook)
                                .concatMap(LoginPresenterImpl.this::saveUser)
                                .compose(LoginPresenterImpl.this.withLoadingView(TRACKING_LOGIN_WITH_FACEBOOK))
                                .subscribe(LoginPresenterImpl.this::onSignInSuccess,
                                        LoginPresenterImpl.this::showErrorMessage)),
                        Throwable::printStackTrace));

        // Handle click on Phone button
        mCompositeDisposable.add(RxView.clicks(mView.getPhoneLoginButton())
                .compose(RxUtil.withLongThrottleFirst())
                .subscribe(o -> AccountKitConnector.getInstance().login(mContext),
                        Throwable::printStackTrace));*//*
    }

    *//*private Observable<Boolean> saveUser(RetrofitResponseItem<UserResponse> userResponse) {
        // Assign flag IsFirstLogin to check if this user is login already or not
        mIsFirstLogin = userResponse.firstLogin;
        User newUser = User.valueOf(userResponse.item);

        // Subscribe this user in order to get new notification from firebase
        // Don't care the result, just call API
        mCompositeDisposable.add(mManager.subscribe(newUser.getAuthToken())
                .compose(RxUtil.withSchedulersIO())
                .subscribe(responseItem1 -> {
                }, Throwable::printStackTrace));

        // Store user
        return UserManager.saveUserAsync(newUser);
    }*//*

    private <R> ObservableTransformer<R, R> withLoadingView(String trackingValue) {
        // Show loading view when button is tapped (when observable is subscribed)
        // Hide loading view when observable is stopped (terminate)
        *//*return upstream -> upstream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    Log.d(TAG, "doOnSubscribe: " + Thread.currentThread().getName());
                    mView.showLoadingView(true);
                    mTracking.track(trackingValue);
                })
                .doOnTerminate((Action) () -> {
                    Log.d(TAG, "doOnTerminal: " + Thread.currentThread().getName());
                    mView.showLoadingView(false);
                });*//*
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
            startUpdateProfileActivity();
        } else {
            startMainActivity();
        }
    }

    private void showErrorMessage(Throwable throwable) {
        Log.d(TAG, "onSignInFailed: " + Thread.currentThread().getName());
        throwable.printStackTrace();
        mView.showErrorMessage(throwable);
    }

    private void startUpdateProfileActivity() {
        mContext.startActivity(UpdateProfileActivity.getInstance(mContext));
    }

    private void startMainActivity() {
        ((Activity) mContext).finish();
        mContext.startActivity(MainActivity.getInstance(mContext));
    }*/
}
