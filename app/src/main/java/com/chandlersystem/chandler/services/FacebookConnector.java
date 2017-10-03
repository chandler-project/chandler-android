package com.chandlersystem.chandler.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chandlersystem.chandler.RxBus;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import io.reactivex.Observable;
public class FacebookConnector {
    private static final String TAG = FacebookConnector.class.getSimpleName();

    private static volatile FacebookConnector sInstance;

    private CallbackManager mFacebookCallbackManager = CallbackManager.Factory.create();

    public static FacebookConnector getInstance() {
        FacebookConnector result = sInstance;
        if (result == null) {
            // Handle case: another thread create new instance before current thread go out of scope
            // Ex: Two thread coming into this: thread A, thread B
            // When thread A completely created a instance , thread B come and doesn't see result != null
            // Then B will see check null again one more time, then A ! null
            // Then B will return as @result
            synchronized (FacebookConnector.class) {
                result = sInstance;
                if (result == null) {
                    sInstance = result = new FacebookConnector();
                }
            }
        }

        return result;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void logOut() {
        LoginManager.getInstance().logOut();
    }

    public Observable<LoginResult> logIn(Context context) {
        return Observable.create(emitter -> {
            // Call Facebook Login
            LoginManager.getInstance().logInWithReadPermissions((Activity) context,
                    Arrays.asList("public_profile", "email", "user_birthday"));

            // Register Facebook getFacebookCallbackManage491022r
            LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                    new FacebookCallback<LoginResult>() {
                        ProfileTracker profileTracker;

                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            if (Profile.getCurrentProfile() == null) {
                                profileTracker = new ProfileTracker() {
                                    @Override
                                    protected void onCurrentProfileChanged(Profile oldProfile,
                                                                           Profile currentProfile) {
                                        if (currentProfile != null && loginResult != null) {
                                            emitter.onNext(loginResult);
                                            emitter.onComplete();
                                        }
                                    }
                                };
                            } else {
                                emitter.onNext(loginResult);
                                emitter.onComplete();
                            }
                        }

                        @Override
                        public void onCancel() {
                            emitter.onComplete();
                        }

                        @Override
                        public void onError(FacebookException error) {
                            emitter.onError(error);
                        }
                    });
        });
    }
}
