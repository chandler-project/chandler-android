package com.chandlersystem.chandler.ui.profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.chandlersystem.chandler.GlideApp;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityEditProfileBinding;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding mBinding;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    private static final int REQUEST_CODE_PICK_PHOTO = 10;

    public static Intent getInstance(Context context) {
        return new Intent(context, EditProfileActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        hideSoftkey();
        setupViews();
        handleEvents();
    }

    private void hideSoftkey() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMediaPermission();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_PHOTO: {
                if (resultCode == RESULT_OK) {
                    String imagePath = data.getData().toString();
                    setupNewAvatar(imagePath);
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));

        mCompositeDisposable.add(editAvatarClicks()
                .subscribe(o -> startImagePicker(), Throwable::printStackTrace));

        mCompositeDisposable.add(buttonSaveClicks()
                .subscribe(o -> userClickSave(), Throwable::printStackTrace));

        mCompositeDisposable.add(
                Observable.combineLatest(nameChanged(), bioChanged(), (t1, t2) -> t1 || t2).subscribe(aBoolean -> {
                    if (aBoolean) {
                        enableButtonSave();
                    } else {
                        disableButtonSave();
                    }
                }, Throwable::printStackTrace));
    }

    private void disableButtonSave() {
        mBinding.layoutNext.btnPrimary.setEnabled(false);
        mBinding.layoutNext.btnPrimary.setBackgroundColor(ContextCompat.getColor(this, R.color.colorInactive));
    }

    private void enableButtonSave() {
        mBinding.layoutNext.btnPrimary.setEnabled(true);
        mBinding.layoutNext.btnPrimary.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void userClickSave() {

    }

    private Observable<Object> editAvatarClicks() {
        return RxView.clicks(mBinding.tvEditAvatar);
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }

    private Observable<Object> buttonSaveClicks() {
        return RxView.clicks(mBinding.layoutNext.btnPrimary);
    }

    private Observable<Boolean> nameChanged() {
        return RxTextView.textChanges(mBinding.etName).map(charSequence -> {
            int textLength = charSequence.toString().length();
            return textLength >= 6 && textLength <= 100;
        });
    }

    private Observable<Boolean> bioChanged() {
        return RxTextView.textChanges(mBinding.etName).map(charSequence -> {
            int textLength = charSequence.toString().length();
            return textLength >= 6 && textLength <= 300;
        });
    }

    private void setupViews() {
        setupToolbar();
        showUserInformation();
        setupButtonSave();
        clearForcusEditText();
    }

    private void clearForcusEditText() {
        mBinding.etName.clearFocus();
        mBinding.etDescription.clearFocus();
    }

    private void setupButtonSave() {
        mBinding.layoutNext.btnPrimary.setText(getString(R.string.content_save));
    }

    private void showUserInformation() {

    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText(getString(R.string.content_edit_profile));
    }

    public void startImagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_PICK_PHOTO);
    }

    private void setupNewAvatar(String imagePath) {
        Uri imagePathUri = Uri.parse(imagePath);
        GlideApp.with(this)
                .load(imagePathUri)
                .into(mBinding.imageView);
    }

    private void checkMediaPermission() {
        int permission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (PackageManager.PERMISSION_GRANTED != permission) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}
