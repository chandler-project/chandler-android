package com.chandlersystem.chandler.services;

import android.content.Context;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.di.components.DaggerServiceComponent;
import com.chandlersystem.chandler.di.components.ServiceComponent;
import com.chandlersystem.chandler.di.modules.ServiceModule;
import com.chandlersystem.chandler.di.scopes.ApplicationContext;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ChandlerInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = ChandlerInstanceIDService.class.getSimpleName();
    private static final String ACTION_START_SESSION = "start_session";

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    ChandlerApi mApi;

    @Inject
    @ApplicationContext
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceComponent component = DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(this))
                .applicationComponent(ChandlerApplication.getApplicationComponent(this))
                .build();
        component.inject(this);
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.clear();
        mCompositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtil.logI(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    // send token to server
    private void sendRegistrationToServer(String refreshedToken) {

    }
}
