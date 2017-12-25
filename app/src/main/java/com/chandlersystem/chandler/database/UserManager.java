package com.chandlersystem.chandler.database;

import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public final class UserManager {
    private static final String TAG = UserManager.class.getSimpleName();

    private UserManager() {
    }

    public static Observable<Boolean> saveUserAsync(User user) {
        return user.save().toObservable();
    }

/*
    public static Observable<Boolean> updateUserAsync(User newUser) {
        User oldUser = getUserSync();
        newUser.setAuthToken(oldUser.getAuthToken());
        newUser.setKnowGameRule(oldUser.isKnowGameRule());
        return newUser.update().toObservable();
    }
*/

    public static User getUserSync() {
        return SQLite.select().from(User.class).limit(1).querySingle();
    }

    public static Observable<User> getUserAsync() {
        return RXSQLite.rx(
                SQLite.select().from(User.class).limit(1))
                .querySingle()
                .toObservable()
                .subscribeOn(Schedulers.io());
    }

    public static boolean isUserLoggedIn() {
        return getUserSync() != null;
    }
}
