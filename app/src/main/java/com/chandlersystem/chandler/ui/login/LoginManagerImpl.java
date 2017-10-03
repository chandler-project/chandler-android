package com.chandlersystem.chandler.ui.login;

import android.content.Context;

import com.chandlersystem.chandler.di.scopes.ActivityContext;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
public class LoginManagerImpl implements LoginManager {
    private static final String ACTION_LOGIN = "login";

    @Inject
    @ActivityContext
    Context mContext;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    LoginManagerImpl() {
    }
}
