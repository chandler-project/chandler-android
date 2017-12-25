package com.chandlersystem.chandler;

import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.BuildConfig;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.chandlersystem.chandler.configs.FirebaseConstant;
import com.chandlersystem.chandler.database.ChandlerDatabase;
import com.chandlersystem.chandler.database.DatabaseHelper;
import com.chandlersystem.chandler.di.components.ApplicationComponent;
import com.chandlersystem.chandler.di.components.DaggerApplicationComponent;
import com.chandlersystem.chandler.di.modules.ApplicationModule;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessaging;

import io.fabric.sdk.android.Fabric;

public class ChandlerApplication extends MultiDexApplication {
    private static final String TAG = ChandlerApplication.class.getSimpleName();

    private ApplicationComponent mApplicationComponent;

    public static ApplicationComponent getApplicationComponent(Context context) {
        return ((ChandlerApplication) context
                .getApplicationContext())
                .getApplicationComponent();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        DatabaseHelper.initDatabase(this);

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            AppEventsLogger.activateApp(this);
        }

        // Integrate with Fabric
        Fabric.with(this, new Crashlytics());

        // Subscribe Firebase to topic "everyone"
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseConstant.TOPIC_EVERYONE);
        FirebaseCrash.setCrashCollectionEnabled(!BuildConfig.DEBUG);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
