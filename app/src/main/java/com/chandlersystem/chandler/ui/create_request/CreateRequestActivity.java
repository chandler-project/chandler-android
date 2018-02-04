package com.chandlersystem.chandler.ui.create_request;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.RxBus;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.events.CreateDealSuccess;
import com.chandlersystem.chandler.data.events.CreateRequestSuccess;
import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.data.models.pojo.FileUpload;
import com.chandlersystem.chandler.data.models.pojo.UploadImage;
import com.chandlersystem.chandler.data.models.request.BudgetBody;
import com.chandlersystem.chandler.data.models.request.CreateDealBody;
import com.chandlersystem.chandler.data.models.request.CreateRequestBody;
import com.chandlersystem.chandler.data.models.response.RetrofitResponseItem;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityCreateRequestBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.RequestPagerAdapter;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreateRequestActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        CompleteCreateDealFragment.CompleteCreateDealListener,
        SelectCategoryFragment.SelectCategoryListener,
        SelectDateFragment.SelectDateListener,
        SelectPriceFragment.SelectPriceListener, SelectCityFragment.SelectCityListener,
        SelectAmountFragment.SelectAmountListener {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    private ActivityCreateRequestBinding mBinding;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private RequestPagerAdapter mDealPagerAdapter;

    private Category mCategory;
    private String mDate;
    private String mPrice;
    private String mCity;
    private String mAmount;

    @Inject
    ChandlerApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_request);

        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();

        mActivityComponent.inject(this);

        setupViews();
        setupViewPager();
        handleEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }

    private void setupViews() {
        mBinding.progress.setMax(RequestPagerAdapter.TOTAL_PAGE);
        mBinding.layoutToolbar.tvTitle.setText(getString(R.string.content_create_new_request));
    }

    private void setupViewPager() {
        mDealPagerAdapter = new RequestPagerAdapter(getSupportFragmentManager(), this);
        mBinding.viewpager.setOffscreenPageLimit(RequestPagerAdapter.TOTAL_PAGE);
        mBinding.viewpager.setAdapter(mDealPagerAdapter);
        mBinding.viewpager.addOnPageChangeListener(this);
        setCurrentItem(0);
        mBinding.viewpager.setPagingEnabled(false);
    }

    private void setCurrentItem(int position) {
        mBinding.viewpager.setCurrentItem(position);
        setCurrentProgress(position + 1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentProgress(position + 1);

    }

    private void setCurrentProgress(int currentProgress) {
        mBinding.progress.setProgress(currentProgress);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void goToNextPage() {
        int currentPage = mBinding.viewpager.getCurrentItem();
        setCurrentItem(currentPage + 1);
    }

    @Override
    public void onCategorySelected(Category category) {
        mCategory = category;
        goToNextPage();
    }

    @Override
    public void onDateSelected(String date) {
        mDate = date;
        goToNextPage();
    }

    @Override
    public void onPriceInserted(String price) {
        mPrice = price;
        goToNextPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkMediaPermission();
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

    @Override
    public void onBackPressed() {
        goToPreviousPage();
    }

    private void goToPreviousPage() {
        int currentPage = mBinding.viewpager.getCurrentItem();
        if (currentPage > 0) {
            setCurrentItem(currentPage - 1);
        } else {
            finish();
        }
    }

    @Override
    public void onCitySelected(String city) {
        mCity = city;
        goToNextPage();
    }

    @Override
    public void onProductCreated(CreateDealBody request) {
        List<String> imagePathList = request.getProductPics();
        List<MultipartBody.Part> filePartList = new ArrayList<>();
        for (String imagePath : imagePathList) {
            MultipartBody.Part part = getRequestBody(new File(getRealPathFromURI(imagePath)));
            filePartList.add(part);
        }

        mCompositeDisposable.add(mApi.uploadImages(UserManager.getUserSync().getAuthorization(), filePartList)
                .compose(RxUtil.withSchedulers())
                .doOnSubscribe(disposable -> ViewUtil.toggleView(mBinding.layoutProgressBar.progressBar, true))
                .subscribe(responseItem -> createRequestApi(responseItem, request), throwable -> {
                    ViewUtil.toggleView(mBinding.layoutProgressBar.progressBar, false);
                    DialogUtil.showErrorDialog(this, throwable);
                }));
    }

    private void createRequestApi(RetrofitResponseItem<UploadImage> result, CreateDealBody request) {
        List<String> urlList = new ArrayList<>();
        for (FileUpload fileUpload : result.item.getResult().getFiles().getFileUpload()) {
            urlList.add("http://etylash.xyz:3001/" + fileUpload.getName());
        }

        CreateRequestBody createRequestBody = CreateRequestBody.valueOf(request);
        createRequestBody.setProductPics(urlList);
        createRequestBody.setAddress(mCity);
        if (mPrice.contains(" - ")) {
            createRequestBody.setBudget(new BudgetBody(Float.valueOf(mPrice.split(" - ")[0])
                    , Float.valueOf(mPrice.split(" - ")[1])));
        } else {
            createRequestBody.setBudget(new BudgetBody(Float.valueOf(mPrice), Float.valueOf(mPrice)));
        }

        createRequestBody.setDeadline(mDate);
        createRequestBody.setAmount(Integer.parseInt(mAmount));

        mCompositeDisposable.add(mApi.createRequest(createRequestBody, mCategory.getId(), UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .doOnTerminate(() -> ViewUtil.toggleView(mBinding.layoutProgressBar.progressBar, false))
                .subscribe(response -> onCreateDealSuccessfully(),
                        throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void onCreateDealSuccessfully() {
        Toast.makeText(CreateRequestActivity.this, getString(R.string.content_create_new_request_successfully), Toast.LENGTH_SHORT).show();
        RxBus.getInstance().post(new CreateDealSuccess());
        finish();
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
    public void onAmountSelected(String amount) {
        mAmount = amount;
        goToNextPage();
    }
}
