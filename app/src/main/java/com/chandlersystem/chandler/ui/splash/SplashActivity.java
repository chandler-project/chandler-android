package com.chandlersystem.chandler.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.database.DatabaseHelper;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivitySplashBinding;
import com.chandlersystem.chandler.ui.adapters.FragmentAdapter;
import com.chandlersystem.chandler.ui.login.LoginActivity;
import com.chandlersystem.chandler.ui.main.MainActivity;
import com.chandlersystem.chandler.ui.splash.onboarding.FirstOnboardingFragment;
import com.chandlersystem.chandler.ui.splash.onboarding.FourthOnboardingFragment;
import com.chandlersystem.chandler.ui.splash.onboarding.SecondOnboardingFragment;
import com.chandlersystem.chandler.ui.splash.onboarding.ThirdOnboardingFragment;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.facebook.Profile;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private ActivitySplashBinding mBinding;

    private FragmentAdapter mViewpagerAdapter;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static Intent getInstance(Context context) {
        Intent i = new Intent(context, SplashActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        DatabaseHelper.initDatabase(this);
        if (UserManager.isUserLoggedIn()) {
            startMainActivity();
        } else {
            setupView();
            setupViewPager();
            handleEvents();
        }
    }

    private void handleEvents() {
        mCompositeDisposable.add(
                RxView.clicks(mBinding.layoutButtonLoginAsGuest.btnPrimary)
                        .compose(RxUtil.withLongThrottleFirst())
                        .subscribe(o -> startMainActivity()));

        mCompositeDisposable.add(
                RxView.clicks(mBinding.tvLogin)
                        .compose(RxUtil.withLongThrottleFirst())
                        .subscribe(o -> startLoginActivity()));
    }

    private void startMainActivity() {
        startActivity(MainActivity.getInstance(this));
        finish();
    }

    private void startLoginActivity() {
        startActivity(LoginActivity.getInstance(this));
        finish();
    }

    private void setupViewPager() {
        List<Fragment> listOnboardingFragment = new ArrayList<>();
        FirstOnboardingFragment firstOnboardingFragment = FirstOnboardingFragment.newInstance();
        SecondOnboardingFragment secondOnboardingFragment = SecondOnboardingFragment.newInstance();
        ThirdOnboardingFragment thirdOnboardingFragment = ThirdOnboardingFragment.newInstance();
        FourthOnboardingFragment fourthOnboardingFragment = FourthOnboardingFragment.newInstance();
        listOnboardingFragment.add(firstOnboardingFragment);
        listOnboardingFragment.add(secondOnboardingFragment);
        listOnboardingFragment.add(thirdOnboardingFragment);
        listOnboardingFragment.add(fourthOnboardingFragment);
        mViewpagerAdapter = new FragmentAdapter(getSupportFragmentManager(), listOnboardingFragment);
        mBinding.viewpagerOnBoarding.setAdapter(mViewpagerAdapter);
        mBinding.indicator.setViewPager(mBinding.viewpagerOnBoarding);
        mViewpagerAdapter.registerDataSetObserver(mBinding.indicator.getDataSetObserver());
    }

    private void setupView() {
        if (mBinding == null) {
            return;
        }

        mBinding.layoutButtonLoginAsGuest.btnPrimary.setText(getString(R.string.content_login_as_guest));
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        mCompositeDisposable.dispose();
        super.onDestroy();
    }
}
