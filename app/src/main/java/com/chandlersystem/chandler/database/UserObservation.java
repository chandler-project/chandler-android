package com.chandlersystem.chandler.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.raizlabs.android.dbflow.runtime.DirectModelNotifier;
import com.raizlabs.android.dbflow.structure.BaseModel;

import io.reactivex.Observable;

public class UserObservation {
    private static final String TAG = UserObservation.class.getCanonicalName();
    private DirectModelNotifier.ModelChangedListener<User> mUserChangedListener;

    public Observable<User> register() {
        return Observable.create(e -> {
            // Emit first snap shot
            UserManager.getUserAsync()
                    .subscribe(e::onNext, Throwable::printStackTrace);

            if (mUserChangedListener == null) {
                mUserChangedListener = new DirectModelNotifier.ModelChangedListener<User>() {
                    @Override
                    public void onModelChanged(@NonNull User user,
                                               @NonNull BaseModel.Action action) {
                        LogUtil.logD(TAG, user.toString());
                        e.onNext(user);
                    }

                    @Override
                    public void onTableChanged(@Nullable Class<?> aClass,
                                               @NonNull BaseModel.Action action) {
                        LogUtil.logD(TAG, aClass != null ? aClass.getCanonicalName() : null);
                    }
                };
            }

            DirectModelNotifier.get()
                    .registerForModelChanges(User.class, mUserChangedListener);
        });
    }

    public void unregister() {
        if (mUserChangedListener != null) {
            DirectModelNotifier.get()
                    .unregisterForModelChanges(User.class, mUserChangedListener);
            mUserChangedListener = null;
        }
    }
}
