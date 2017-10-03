package com.chandlersystem.chandler.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.chandlersystem.chandler.AccountKitException;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.RxBus;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;

import io.reactivex.Observable;

import static android.app.Activity.RESULT_OK;

public class AccountKitConnector {
    private static final String TAG = AccountKitConnector.class.getSimpleName();

    private static int REQUEST_PHONE_LOGIN = 99;

    private static volatile AccountKitConnector sInstance;

    public static AccountKitConnector getInstance() {
        AccountKitConnector result = sInstance;
        if (result == null) {
            // Handle case: another thread create new instance before current thread go out of scope
            // Ex: Two thread coming into this: thread A, thread B
            // When thread A completely created a instance , thread B come and doesn't see result != null
            // Then B will see check null again one more time, then A ! null
            // Then B will return as @result
            synchronized (RxBus.class) {
                result = sInstance;
                if (result == null) {
                    sInstance = result = new AccountKitConnector();
                }
            }
        }

        return result;
    }

    public Observable<AccessToken> onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        return Observable.create(e -> {
            if (requestCode == REQUEST_PHONE_LOGIN && resultCode == RESULT_OK) {
                AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

                if (loginResult.getError() != null) {
                    e.onError(new AccountKitException(activity.getString(R.string.error_common)));
                } else if (loginResult.wasCancelled()) {
                    e.onComplete();
                } else {
                    AccessToken accessToken = loginResult.getAccessToken();
                    if (accessToken != null) {
                        e.onNext(accessToken);
                        e.onComplete();
                    } else {
                        e.onError(new AccountKitException(activity.getString(R.string.error_common)));
                    }
                }
            } else {
                e.onComplete();
            }
        });
    }

    public void login(Context context) {

        final Intent intent = new Intent(context, AccountKitActivity.class);

        // Customize Account Kit UI
        UIManager uiManager = new SkinManager(
                SkinManager.Skin.CLASSIC,
                ContextCompat.getColor(context, R.color.colorGreen));

        // Customize Account Kit behavior
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN)
                        .setDefaultCountryCode("VN")
                        .setUIManager(uiManager);

        // Create and start intent
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        ((Activity) context).startActivityForResult(intent, REQUEST_PHONE_LOGIN);
    }

    public void logout() {
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            AccountKit.logOut();
        }
    }
}
