package com.chandlersystem.chandler.ui.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.data.models.request.BidRequestBody;
import com.chandlersystem.chandler.data.models.request.Device;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityMainBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.FragmentAdapter;
import com.chandlersystem.chandler.ui.cart.CartActivity;
import com.chandlersystem.chandler.ui.create_deal.CreateDealActivity;
import com.chandlersystem.chandler.ui.create_request.CreateRequestActivity;
import com.chandlersystem.chandler.ui.deal.DealFragment;
import com.chandlersystem.chandler.ui.dialogs.BidDialog;
import com.chandlersystem.chandler.ui.main.state.MainState;
import com.chandlersystem.chandler.ui.notification.NotificationFragment;
import com.chandlersystem.chandler.ui.product_search.ProductSearchActivity;
import com.chandlersystem.chandler.ui.profile.ProfileFragment;
import com.chandlersystem.chandler.ui.requests.RequestsFragment;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener, DealFragment.OnFragmentInteractionListener,
        CreateDealRequestFragment.OnCreateDealRequestInteraction, BidDialog.BidDialogCallback {
    private static final String TAG = MainActivity.class.getCanonicalName();

    private ActivityMainBinding mBinding;

    private MainState mMainState;

    private FragmentAdapter mFragmentAdapter;

    private ActivityComponent mActivityComponent;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent
                    .builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(ChandlerApplication
                            .getApplicationComponent(this))
                    .build();
        }

        return mActivityComponent;
    }

    @Inject
    ChandlerApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getActivityComponent().inject(this);
        setupViews();
        handleEvents();
        subscribeNotification();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCompositeDisposable.add(mApi.getPendingOrderCount(UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(o -> showBadge(o.item.getCount()), Throwable::printStackTrace));
    }

    private void showBadge(int count) {
        if (count > 0) {
            ViewUtil.setText(mBinding.toolbar.tvCartCount, count + "");
            ViewUtil.toggleView(mBinding.toolbar.tvCartCount, true);
        } else {
            ViewUtil.toggleView(mBinding.toolbar.tvCartCount, false);
        }

    }

    private void subscribeNotification() {
        User user = UserManager.getUserSync();
        if (user == null) {
            return;
        }

        String registrationId = FirebaseInstanceId.getInstance().getToken();
        Device device = new Device(this, registrationId);
        mApi.subscribeRegistrationId(device, user.getId(), user.getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(retrofitResponseItem -> {

                }, Throwable::printStackTrace);
    }

    private void handleEvents() {
        mBinding.navHome.setOnClickListener(this);
        mBinding.navRequest.setOnClickListener(this);
        mBinding.navCreate.setOnClickListener(this);
        mBinding.navNotification.setOnClickListener(this);
        mBinding.navProfile.setOnClickListener(this);
        mBinding.viewpager.addOnPageChangeListener(this);
        mBinding.toolbar.ivCart.setOnClickListener(this);
        mBinding.toolbar.ivSearch.setOnClickListener(this);
    }

    private void setupViews() {
        mMainState = new MainState(this, mBinding, 0);

        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(DealFragment.newInstance());
        fragmentList.add(RequestsFragment.newInstance(1));
        fragmentList.add(CreateDealRequestFragment.newInstance());
        fragmentList.add(NotificationFragment.newInstance());
        fragmentList.add(ProfileFragment.newInstance());
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        mBinding.viewpager.setAdapter(mFragmentAdapter);
        mBinding.viewpager.setOffscreenPageLimit(5);
        setFragmentPosition(0);
    }

    public static Intent getInstance(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        return i;
    }

    private void setFragmentPosition(int position) {
        mMainState.navigate(position);
        mBinding.viewpager.setCurrentItem(position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_home:
                setFragmentPosition(0);
                break;
            case R.id.nav_request:
                setFragmentPosition(1);
                break;
            case R.id.nav_create:
                setFragmentPosition(2);
                break;
            case R.id.nav_notification:
                setFragmentPosition(3);
                break;
            case R.id.nav_profile:
                setFragmentPosition(4);
                break;
            case R.id.iv_cart:
                startCartActivity();
                break;
            case R.id.iv_search:
                openProductSearch();
                break;
        }
    }

    private void startCartActivity() {
        startActivity(CartActivity.getInstance(this));
    }

    private void openProductSearch() {
        Intent i = ProductSearchActivity.getIntent(this);
        startActivity(i);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setFragmentPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCreateRequest() {
        Intent i = new Intent(this, CreateRequestActivity.class);
        startActivity(i);
    }

    @Override
    public void onCreateDeal() {
        boolean isShipper = UserManager.getUserSync().isShipper();
        if (!isShipper) {
            Toast.makeText(this, getString(R.string.content_you_have_to_be_shipper_to_create_deal), Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(this, CreateDealActivity.class);
            startActivity(i);
        }
    }

    private void callBidApi(String requestId, BidRequestBody bidRequestBody) {
        mCompositeDisposable.add(mApi.bidRequest(bidRequestBody, requestId, UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(retrofitResponseListItem -> bidSuccess(retrofitResponseListItem.item), this::bidError));
    }

    private void bidSuccess(Request item) {
        Toast.makeText(this, getString(R.string.content_bid_sucess), Toast.LENGTH_SHORT).show();
    }

    private void bidError(Throwable throwable) {
        Toast.makeText(this, getString(R.string.content_bid_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBid(String requestId, BidRequestBody bidRequestBody) {
        callBidApi(requestId, bidRequestBody);
    }
}
