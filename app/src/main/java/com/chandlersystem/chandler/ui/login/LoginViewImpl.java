package com.chandlersystem.chandler.ui.login;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityLoginBinding;
import com.chandlersystem.chandler.di.scopes.ActivityContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class LoginViewImpl implements LoginView {
    private final String TAG = LoginViewImpl.class.getSimpleName();

    private static final long DURATION_LOADING_INDICATOR = 250;

    private ActivityLoginBinding mBinding;

    private final List<ImageView> mIndicators = new ArrayList<>();

    private Observable<Long> mLoadingObservable =
            Observable.interval(DURATION_LOADING_INDICATOR, TimeUnit.MILLISECONDS);

    private Disposable mLoadingSubscription;

    @Inject
    @ActivityContext
    Context mContext;

    @Inject
    LoginViewImpl() {
    }

    @Override
    public void bindView() {
        mBinding = DataBindingUtil.setContentView(
                (Activity) mContext, R.layout.activity_login);
    }
}
