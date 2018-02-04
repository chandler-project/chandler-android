package com.chandlersystem.chandler.ui.profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.GlideApp;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.UploadImage;
import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.data.models.request.EditProfileBody;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityEditProfileBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding mBinding;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    private static final int REQUEST_CODE_PICK_PHOTO = 10;

    public static Intent getInstance(Context context) {
        return new Intent(context, EditProfileActivity.class);
    }

    @Inject
    ChandlerApi mApi;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);

        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();

        mActivityComponent.inject(this);

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
                    updateUserAvatar(new File(getRealPathFromURI(imagePath)));
                }
                break;
            }
        }
    }

    private MultipartBody.Part getRequestBody(File imageFile) {
        RequestBody request = RequestBody.create(MediaType.parse("image/*"), imageFile);

        return MultipartBody.Part.createFormData("fileUpload", imageFile.getName(), request);
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
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

        mCompositeDisposable.add(avatarClicks()
                .subscribe(o -> startImagePicker(), Throwable::printStackTrace));

        mCompositeDisposable.add(
                Observable.combineLatest(nameChanged(), bioChanged(), (t1, t2) -> t1 || t2).subscribe(aBoolean -> {
                    if (aBoolean) {
                        enableButtonSave();
                    } else {
                        disableButtonSave();
                    }
                }, Throwable::printStackTrace));
    }

    private Observable<Object> avatarClicks() {
        return RxView.clicks(mBinding.ivProfile);
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
        User currentUser = UserManager.getUserSync();

        String newName = mBinding.etName.getText().toString();
        String newBio = mBinding.etDescription.getText().toString();
        String newAddress = mBinding.etAddress.getText().toString();
        String newPhoneNumber = mBinding.etPhoneNumber.getText().toString();

        EditProfileBody body = new EditProfileBody();
        if (ValidateUtil.checkString(newName)) {
            body.setName(newName);
        }

        if (ValidateUtil.checkString(newBio)) {
            body.setBio(newBio);
        }

        if (ValidateUtil.checkString(newPhoneNumber)) {
            body.setPhoneNumber(newPhoneNumber);
        }

        if (ValidateUtil.checkString(newAddress)) {
            body.setAddress(newAddress);
        }

        mCompositeDisposable.add(mApi.editProfile(body, currentUser.getId(), currentUser.getAuthorization())
                .compose(RxUtil.withSchedulers())
                .doOnSubscribe(disposable -> showProgressBar())
                .doOnTerminate(this::hideProgressBar)
                .subscribe(retrofitResponseItem -> updateProfileSuccessfully(retrofitResponseItem.item),
                        throwable -> DialogUtil.showErrorDialog(EditProfileActivity.this, throwable)));
    }

    private void updateUserAvatar(File file) {
        User currentUser = UserManager.getUserSync();
        MultipartBody.Part newAvatarFile = getRequestBody(file);

        mCompositeDisposable.add(mApi.uploadImage(currentUser.getAuthorization(), newAvatarFile)
                .compose(RxUtil.withSchedulers())
                .doOnSubscribe(disposable -> showProgressBar())
                .doOnTerminate(this::hideProgressBar)
                .subscribe(retrofitResponseItem -> updateAvatarSuccessfully(retrofitResponseItem.item),
                        throwable -> DialogUtil.showErrorDialog(EditProfileActivity.this, throwable)));
    }

    private void updateAvatarSuccessfully(UploadImage uploadImage) {
        User currentUser = UserManager.getUserSync();
        currentUser.setAvatar(uploadImage.getResult().getFiles().getFileUpload().get(0).getName());

        mCompositeDisposable.add(currentUser.save()
                .subscribe(aBoolean -> {
                }, Throwable::printStackTrace));

        Toast.makeText(this, getString(R.string.content_update_profile_successfully), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateProfileSuccessfully(User user) {
        User currentUser = UserManager.getUserSync();
        currentUser.setFullName(user.getFullName());
        currentUser.setBio(user.getBio());

        mCompositeDisposable.add(
                currentUser.save().subscribe(aBoolean -> {
                }, Throwable::printStackTrace));

        Toast.makeText(this, getString(R.string.content_update_profile_successfully), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void hideProgressBar() {
        ViewUtil.toggleView(mBinding.layoutProgressBar.progressBar, false);
    }

    private void showProgressBar() {
        ViewUtil.toggleView(mBinding.layoutProgressBar.progressBar, true);
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
        User user = UserManager.getUserSync();
        if (ValidateUtil.checkString(user.getFullName())) {
            mBinding.etName.setText(user.getFullName());
        }

        if (ValidateUtil.checkString(user.getBio())) {
            mBinding.etDescription.setText(user.getBio());
        }

        if (ValidateUtil.checkString(user.getPhoneNumber())) {
            mBinding.etPhoneNumber.setText(user.getPhoneNumber());
        }

        if (ValidateUtil.checkString(user.getAddress())) {
            mBinding.etPhoneNumber.setText(user.getAddress());
        }

        if (ValidateUtil.checkString(user.getAvatar())) {
            ViewUtil.showImage(this, ApiConstant.BASE_URL_VER1 + user.getAvatar(), mBinding.ivProfile);
        }
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
                .into(mBinding.ivProfile);
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
