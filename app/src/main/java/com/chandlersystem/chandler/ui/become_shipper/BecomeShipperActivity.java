package com.chandlersystem.chandler.ui.become_shipper;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityBecomeShipperBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class BecomeShipperActivity extends AppCompatActivity {
    private static final String TAG = BecomeShipperActivity.class.getCanonicalName();

    private ActivityBecomeShipperBinding mBinding;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static Intent getIntent(Context context) {
        return new Intent(context, BecomeShipperActivity.class);
    }

    @Inject
    ChandlerApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_become_shipper);

        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();

        mActivityComponent.inject(this);

        setupViews();
        handleEvents();
    }

    private void handleEvents() {
        mCompositeDisposable.add(RxView.clicks(mBinding.btnBecomeShipper)
                .subscribe(o -> callApiBecomeShipper(), Throwable::printStackTrace));
    }

    private void callApiBecomeShipper() {
        User currentUser = UserManager.getUserSync();
        mCompositeDisposable.add(mApi.becomeShipper(currentUser.getId(), currentUser.getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(retrofitResponseItem -> {
                            currentUser.setShipper(true);
                            saveUser(currentUser);
                        }
                        , throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void saveUser(User currentUser) {
        mCompositeDisposable.add(
                currentUser.save().subscribe(aBoolean -> {

                }, Throwable::printStackTrace));
    }

    private void setupViews() {
        ViewUtil.showImage(this, "https://media.giphy.com/media/l1BgSKQQgjBO80I5q/giphy.gif", mBinding.ivOnboardingImage);
        mBinding.tvTitle.setText(getString(R.string.content_want_to_become_a_shipper));
        mBinding.tvSubTitle.setText(Html.fromHtml(getString(R.string.content_please_read_our_term_and_condition)));
        mBinding.btnBecomeShipper.setText(getString(R.string.content_become_a_shipper));
    }
}
