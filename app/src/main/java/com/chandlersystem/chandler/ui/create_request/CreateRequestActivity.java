package com.chandlersystem.chandler.ui.create_request;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.databinding.ActivityCreateRequestBinding;
import com.chandlersystem.chandler.ui.adapters.DealPagerAdapter;
import com.chandlersystem.chandler.ui.adapters.RequestPagerAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class CreateRequestActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        CompleteCreateDealFragment.CompleteCreateDealListener,
        SelectCategoryFragment.SelectCategoryListener,
        SelectDateFragment.SelectDateListener,
        SelectPriceFragment.SelectPriceListener, SelectCityFragment.SelectCityListener {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    private ActivityCreateRequestBinding mBinding;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private RequestPagerAdapter mDealPagerAdapter;

    private String mProductName;
    private String mProductDetail;
    private Category mCategory;
    private String mDate;
    private String mPrice;
    private String mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_request);
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
        mDealPagerAdapter = new RequestPagerAdapter(getSupportFragmentManager());
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

    @Override
    public void onProductNameAdded(String name) {
        mProductName = name;
        goToNextPage();
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
}
