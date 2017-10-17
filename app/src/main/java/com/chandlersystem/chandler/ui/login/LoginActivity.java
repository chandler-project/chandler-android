package com.chandlersystem.chandler.ui.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityLoginBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.services.AccountKitConnector;
import com.chandlersystem.chandler.services.FacebookConnector;
import com.chandlersystem.chandler.ui.main.MainActivity;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.facebook.accountkit.AccessToken;
import com.facebook.login.LoginResult;
import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private ActivityLoginBinding mBinding;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static Intent getInstance(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();
        mActivityComponent.inject(this);

        mCompositeDisposable.add(RxView.clicks(mBinding.btnFacebookLogin)
                .compose(RxUtil.withLongThrottleFirst())
                .concatMap(o -> FacebookConnector.getInstance().logIn(this))
                .subscribe(loginResult -> startMainActivity(),
                        Throwable::printStackTrace));

        mCompositeDisposable.add(RxView.clicks(mBinding.btnPhoneNumber)
                .compose(RxUtil.withLongThrottleFirst())
                .subscribe(o -> AccountKitConnector.getInstance().login(LoginActivity.this)));
    }

    private void startMainActivity() {
        startActivity(MainActivity.getInstance(this));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (FacebookConnector.getInstance().onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        mCompositeDisposable.add(AccountKitConnector.getInstance()
                .onActivityResult(LoginActivity.this, requestCode, resultCode, data)
                .subscribe(accessToken -> {
                    LogUtil.logD(TAG, "Account kit access token: " + accessToken);
                    startMainActivity();
                }, Throwable::printStackTrace));
    }

    //On Activity Destroy
    @Override
    protected void onDestroy() {
        mCompositeDisposable.dispose();
        mCompositeDisposable.clear();
        super.onDestroy();
    }
}
