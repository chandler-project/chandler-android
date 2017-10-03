package com.chandlersystem.chandler.ui.splash;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivitySplashBinding;
import com.chandlersystem.chandler.ui.login.LoginActivity;
import com.chandlersystem.chandler.ui.main.MainActivity;
import com.chandlersystem.chandler.utilities.AnimUti;

import io.reactivex.disposables.CompositeDisposable;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final Integer SPLASH_SCREEN_DURATION = 500;
    private static final Integer SPLASH_SCREEN_DELAY = 200;

    private ActivitySplashBinding mBinding;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static Intent getInstance(Context context) {
        Intent i = new Intent(context, SplashActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind view
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        //startSplashAnimation();
        startMainActivity();
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        mCompositeDisposable.dispose();
        super.onDestroy();
    }
    public ObjectAnimator getFadeInAnimation() {
        return AnimUti.getFadeAnimation(mBinding.image, SPLASH_SCREEN_DURATION,
                new AccelerateDecelerateInterpolator(), 1f, 0f);
    }

    public void startSplashAnimation() {
        final ObjectAnimator animator = getFadeInAnimation();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //detectNextActivity();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setStartDelay(SPLASH_SCREEN_DELAY);
        animator.start();
    }

    private void detectNextActivity() {
        startMainActivity();
    }

    private void startMainActivity() {
        startActivity(MainActivity.getInstance(this));
        finish();
    }

    private void startLoginActivity() {
        startActivity(LoginActivity.getInstance(this));
        finish();
    }


    /*private void clearDatabase() {
        mCompositeDisposable.add(DatabaseManager.clearTable(User.class)
                .compose(RxUtil.withSchedulers())
                .doOnTerminate(this::startLoginActivity)
                .subscribe(transaction -> {
                }, Throwable::printStackTrace)
        );
    }

    private boolean isUserLoggedIn(String fullName, String birthday) {
        return ValidateUtil.checkString(fullName) && ValidateUtil.checkString(birthday);
    }*/
}
