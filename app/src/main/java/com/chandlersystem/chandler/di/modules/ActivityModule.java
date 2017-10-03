package com.chandlersystem.chandler.di.modules;

import android.app.Activity;
import android.content.Context;

import com.chandlersystem.chandler.di.scopes.ActivityContext;
import com.chandlersystem.chandler.di.scopes.PerActivity;
import com.chandlersystem.chandler.ui.login.LoginManager;
import com.chandlersystem.chandler.ui.login.LoginManagerImpl;
import com.chandlersystem.chandler.ui.login.LoginPresenter;
import com.chandlersystem.chandler.ui.login.LoginPresenterImpl;
import com.chandlersystem.chandler.ui.login.LoginView;
import com.chandlersystem.chandler.ui.login.LoginViewImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    /**
     * Presenter, View, Manager for Login
     */
    @Provides
    @PerActivity
    LoginPresenter provideLoginPresenter(LoginPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LoginView provideLoginView(LoginViewImpl view) {
        return view;
    }

    @Provides
    @PerActivity
    LoginManager provideLoginManager(LoginManagerImpl manager) {
        return manager;
    }
}
