package com.chandlersystem.chandler.di.components;

import android.content.Context;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.di.modules.ApplicationModule;
import com.chandlersystem.chandler.di.scopes.ApplicationContext;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(ChandlerApplication app);

    @ApplicationContext
    Context getApplicationContext();

    ChandlerApi getSevenRewardsApiV1();

    FirebaseDatabase getFirebaseDatabase();
}
