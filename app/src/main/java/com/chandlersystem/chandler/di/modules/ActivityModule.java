package com.chandlersystem.chandler.di.modules;

import android.app.Activity;
import android.content.Context;

import com.chandlersystem.chandler.di.scopes.ActivityContext;
import com.chandlersystem.chandler.di.scopes.PerActivity;
import com.chandlersystem.chandler.ui.login.LoginActivity;

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
}
