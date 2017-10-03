package com.chandlersystem.chandler.services;

import android.app.Activity;

import com.chandlersystem.chandler.BuildConfig;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.FirebaseConstant;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import io.reactivex.Observable;

public class FirebaseManager {
    private static final String TAG = FirebaseManager.class.getSimpleName();

    private static final String VERSION_CODE = "android_force_update_version_code";
    private static final String MESSAGE = "android_force_update_message";

    private FirebaseManager() {

    }

    public static void subscribe(String userCode) {
        // subscribe firebase messaging to topic customer
        String userTopic = FirebaseConstant.TOPIC_CUSTOMER + userCode;
        LogUtil.logD(TAG, "subscribe topic " + userTopic);
        FirebaseMessaging.getInstance().subscribeToTopic(userTopic);
    }

    public static Observable<String> remoteConfig(Activity activity) {
        return Observable.create(e -> {
            FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();
            firebaseRemoteConfig.setConfigSettings(configSettings);
            long cacheExpiration = 3600; // 1 hour in seconds.
            // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
            // retrieve values from the service.
            if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
                cacheExpiration = 0;
            }

            LogUtil.logD(TAG, "Cache expiration: " + cacheExpiration);
            firebaseRemoteConfig.fetch(cacheExpiration)
                    .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            LogUtil.logI(TAG, "remoteConfig successfully");
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            firebaseRemoteConfig.activateFetched();
                        } else {
                            LogUtil.logI(TAG, "remoteConfig failed");
                            e.onError(new Throwable(activity.getString(R.string.error_common)));
                        }

                        double expectedVersion = firebaseRemoteConfig.getDouble(VERSION_CODE);
                        LogUtil.logD(TAG, "Expected version: " + expectedVersion);
                        LogUtil.logD(TAG, "current version: " + BuildConfig.VERSION_CODE);
                        if (BuildConfig.VERSION_CODE < expectedVersion) {
                            e.onNext(firebaseRemoteConfig.getString(MESSAGE));
                            e.onComplete();
                        }
                    });
        });
    }
}
