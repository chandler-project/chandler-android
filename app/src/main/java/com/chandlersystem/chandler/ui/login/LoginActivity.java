package com.chandlersystem.chandler.ui.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.request.LoginRequest;
import com.chandlersystem.chandler.databinding.ActivityLoginBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.select_category.SelectCategoryActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private ActivityLoginBinding mBinding;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private CallbackManager mCallbackManager;
    private ProfileTracker mProfileTracker;

    public static Intent getInstance(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();
        mActivityComponent.inject(this);
        mCallbackManager = CallbackManager.Factory.create();
        mBinding.loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.i(TAG, "onSuccess: fb token: "+loginResult.getAccessToken().getToken());
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            Log.v("facebook - profile", currentProfile.getFirstName());
                            mProfileTracker.stopTracking();
                            authentication(currentProfile);
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    authentication(profile);
                    Log.v("facebook - profile", profile.getFirstName());
                }

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }

    private void authentication(Profile currentProfile) {
        if(currentProfile != null){
            LoginRequest loginRequest = new LoginRequest(AccessToken.getCurrentAccessToken().getToken(), currentProfile.getId());
            ChandlerApi api = ChandlerApplication.getApplicationComponent(LoginActivity.this).getSevenRewardsApiV1();
//            ChandlerApi api = new Retrofit.Builder().baseUrl(ApiConstant.BASE_URL_VER1)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//                    .create(ChandlerApi.class);
            api.authentication(loginRequest, 1, "user").enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Log.i(TAG, "onResponse: login success");
                        startMainActivity();
                    }else{
                        Toast.makeText(LoginActivity.this, "Unknown Error: please try again!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                }
            });
        }
    }

    private void startMainActivity() {
        startActivity(SelectCategoryActivity.getInstance(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    //On Activity Destroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
