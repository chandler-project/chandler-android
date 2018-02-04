package com.chandlersystem.chandler.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.data.models.request.PaymentAllBody;
import com.chandlersystem.chandler.data.models.request.PaymentBody;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityPaymentBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding binding;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    ChandlerApi mApi;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();

        mActivityComponent.inject(this);

        setupToolbar();
        showUserInformation();
        handleEvents();
    }

    private void setupToolbar() {
        binding.layoutToolbar.tvTitle.setText(getString(R.string.content_payment));
    }

    private void handleEvents() {
        compositeDisposable.add(formValidated().subscribe(aBoolean -> {
            if (aBoolean) {
                enableButtonNext();
            } else {
                disableButtonNext();
            }
        }, Throwable::printStackTrace));

        compositeDisposable.add(RxView.clicks(binding.btnPay)
                .subscribe(o -> {
                    callApiPayment();
                }, Throwable::printStackTrace));

        compositeDisposable.add(RxView.clicks(binding.layoutToolbar.ivBack)
                .subscribe(o -> finish(), Throwable::printStackTrace));
    }

    private void callApiPayment() {
        PaymentAllBody paymentAllBody = new PaymentAllBody();
        paymentAllBody.setAddress(binding.etAddress.getText().toString());
        paymentAllBody.setPhoneNumber(binding.etPhone.getText().toString());
        PaymentBody body = new PaymentBody();
        body.setPayment(paymentAllBody);
        compositeDisposable.add(mApi.payment(body, UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(retrofitResponseItem -> {
                    paymentSuccess();
                }, throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void paymentSuccess() {
        finish();
        Toast.makeText(this, getString(R.string.content_payment_success), Toast.LENGTH_SHORT).show();
    }

    private void showError() {
        final Snackbar snackBar = Snackbar.make(binding.getRoot(), R.string.content_please_input_phone_number_and_address, Snackbar.LENGTH_LONG);
        snackBar.setAction("OK", v -> snackBar.dismiss());
        snackBar.show();
    }

    private void disableButtonNext() {
        binding.btnPay.setEnabled(false);
        binding.btnPay.setBackgroundColor(ContextCompat.getColor(this, R.color.colorInactive));
    }

    private void enableButtonNext() {
        binding.btnPay.setEnabled(true);
        binding.btnPay.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private Observable<Boolean> formValidated() {
        return Observable.combineLatest(phoneNumberValidated(), addressValidated(), (aBoolean, aBoolean2) -> aBoolean && aBoolean2);
    }

    private Observable<Boolean> phoneNumberValidated() {
        return RxTextView.textChanges(binding.etPhone)
                .map(charSequence -> {
                    int textLength = charSequence.toString().length();
                    return textLength == 10 || textLength == 11;
                });
    }

    private Observable<Boolean> addressValidated() {
        return RxTextView.textChanges(binding.etAddress)
                .map(charSequence -> {
                    int currentLength = charSequence.toString().length();
                    return (currentLength > 10);
                });
    }

    private void showUserInformation() {
        User user = UserManager.getUserSync();
        ViewUtil.setText(binding.userName, user.getFullName());
        ViewUtil.showImage(this, ApiConstant.BASE_URL_VER1 + user.getAvatar(), binding.ivProfile);

        String phoneNumber = user.getPhoneNumber();
        if (ValidateUtil.checkString(phoneNumber)) {
            binding.etPhone.setText(phoneNumber);
        }

        String address = user.getAddress();
        if (ValidateUtil.checkString(address)) {
            binding.etAddress.setText(address);
        }
    }

    public static Intent getInstance(Context context) {
        return new Intent(context, PaymentActivity.class);
    }
}
