package com.chandlersystem.chandler.ui.create_request;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityCreateRequestBinding;
import com.chandlersystem.chandler.ui.adapters.ItemImageAdapter;
import com.chandlersystem.chandler.utilities.ValidateUtil;

import java.util.List;

public class CreateRequestActivity extends AppCompatActivity {

    private static final String SAVED_IMAGE_LIST = "saved-image-list";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static final int REQUEST_CODE_PICK_PHOTO = 10;
    private static final String TAG = CreateRequestActivity.class.getSimpleName();
    private ActivityCreateRequestBinding mBinding;
    private ItemImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_request);

        setUpLayout(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(SAVED_IMAGE_LIST, mAdapter.getData());
        super.onSaveInstanceState(outState);
    }

    private void setUpLayout(Bundle savedState) {
        mBinding.content.recyclerViewItemImages.setHasFixedSize(true);
        mBinding.content.recyclerViewItemImages.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ItemImageAdapter(this);
        if (savedState != null) {
            List<String> images = savedState.getStringArrayList(SAVED_IMAGE_LIST);
            if (images != null) {
                mAdapter.addImage(images);
            }
        }
        mBinding.content.recyclerViewItemImages.setAdapter(mAdapter);
    }

    public void onUserClickAddImage() {
        Toast.makeText(this, "on user click add image button", Toast.LENGTH_SHORT).show();
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
        } else {
            //TODO start image picker
            startImagePicker();
        }
    }

    public void startImagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_PICK_PHOTO);
    }

    private boolean validateForm() {
        boolean result = true;
        result = validateUrl(mBinding.content.editTextItemUrl.getText().toString(), mBinding.content.inputItemUrl) && result;
        result = ValidateUtil.validateEmptyInput(this, mBinding.content.editTextItemName, mBinding.content.inputItemName) && result;
        result = validateItemImage() && result;
        return result;
    }

    private boolean validateItemImage() {
        if (mAdapter.getItemCount() <= 1) {
            Snackbar.make(mBinding.getRoot(), R.string.upload_picture_image_warning, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateUrl(String url, @Nullable TextInputLayout inputLayout) {
        boolean result;
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            if (inputLayout != null)
                inputLayout.setError(getString(R.string.invalid_item_url_warning));
            result = false;
        } else {
            result = true;
            if (inputLayout != null)
                inputLayout.setError(null);
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_PICK_PHOTO: {
                if (resultCode == RESULT_OK) {
                    String imagePath = data.getData().toString();
                    Log.i(TAG, "onActivityResult: user selected image path: " + imagePath);
                    mAdapter.addImage(imagePath);
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startImagePicker();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Snackbar.make(mBinding.getRoot(),
                            R.string.explain_read_storage_permission,
                            Snackbar.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
