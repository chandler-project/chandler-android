package com.chandlersystem.chandler.ui.order;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.Order;
import com.chandlersystem.chandler.data.models.pojo.User;
import com.chandlersystem.chandler.data.models.request.OrderBody;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityOrderBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.OrderAdapter;
import com.chandlersystem.chandler.ui.dialogs.ReasonDialog;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class OrderActivity extends AppCompatActivity implements ReasonDialog.ReasonDialogCallback {

    private ActivityOrderBinding mBinding;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private OrderAdapter mAdapter;

    private List<Order> mOrderList;

    private final User mUser = UserManager.getUserSync();

    private Order mCurrentOrder;

    @Inject
    ChandlerApi mApi;

    public static Intent getInstance(Context context) {
        return new Intent(context, OrderActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order);

        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();

        mActivityComponent.inject(this);

        setupViews();
        handleEvents();
        getOrderApi();
    }

    private void getOrderApi() {
        User user = UserManager.getUserSync();
        String filter = String.format("{\"order\": \"modified DESC\",\"where\": {\"status\": {\"neq\": \"pending\"}, \"or\": [{\"requester.id\": \"%s\"}, {\"shipper.id\": \"%s\"}]}}", user.getId(), user.getId());
        mCompositeDisposable.add(mApi.getOrder(user.getAuthorization(), filter)
                .compose(RxUtil.withSchedulers())
                .subscribe(retrofitResponseListItem -> setAdapter(retrofitResponseListItem.items), Throwable::printStackTrace));
    }

    private void handleEvents() {
        mCompositeDisposable.add(RxSwipeRefreshLayout.refreshes(mBinding.layoutSwipe)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    mBinding.layoutSwipe.setRefreshing(false);
                    getOrderApi();
                }, Throwable::printStackTrace));

        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }

    private void setupViews() {
        setupToolbar();
        setupCartRecyclerView();
    }

    private void setupCartRecyclerView() {
        mBinding.rvCart.setHasFixedSize(true);
        mBinding.rvCart.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setAdapter(List<Order> orderList) {
        mOrderList = orderList;
        mAdapter = new OrderAdapter(this, mOrderList, UserManager.getUserSync().getId());
        mBinding.rvCart.setAdapter(mAdapter);

        orderClicks();
    }

    private void orderClicks() {
        mCompositeDisposable.add(mAdapter.getOrderClicks().subscribe(this::handleOrderState, Throwable::printStackTrace));
    }

    private void handleOrderState(Order order) {
        mCurrentOrder = order;
        switch (order.getButtonId()) {
            case R.id.btn_accept_delivery:
                acceptDeliver(order);
                break;
            case R.id.btn_reject_delivery:
                DialogUtil.showReasonDialog(this);
                break;
            case R.id.btn_delivered:
                deliveredOrder(order);
                break;
            case R.id.btn_accept_receive:
                confirmOrder(order);
                break;
            case R.id.btn_reject_receive:
                DialogUtil.showReasonDialog(this);
                break;
        }
    }

    private void deniedOrder(Order order, String body) {
        mCompositeDisposable.add(mApi.deniedOrder(body, order.getId(), mUser.getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(orderRetrofitResponseItem -> {
                    int indexOf = mOrderList.indexOf(order);
                    mOrderList.set(indexOf, orderRetrofitResponseItem.item);
                    mAdapter.notifyItemChanged(indexOf);
                }, throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void rejectDeliver(Order order, String body) {
        mCompositeDisposable.add(mApi.rejectDelivery(body, order.getId(), mUser.getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(orderRetrofitResponseItem -> {
                    int indexOf = mOrderList.indexOf(order);
                    mOrderList.set(indexOf, orderRetrofitResponseItem.item);
                    mAdapter.notifyItemChanged(indexOf);
                }, throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void confirmOrder(Order order) {
        mCompositeDisposable.add(mApi.confirmedOrder(order.getId(), mUser.getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(orderRetrofitResponseItem -> {
                    int indexOf = mOrderList.indexOf(order);
                    mOrderList.set(indexOf, orderRetrofitResponseItem.item);
                    mAdapter.notifyItemChanged(indexOf);
                }, throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void deliveredOrder(Order order) {
        mCompositeDisposable.add(mApi.deliveredOrder(order.getId(), mUser.getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(orderRetrofitResponseItem -> {
                    int indexOf = mOrderList.indexOf(order);
                    mOrderList.set(indexOf, orderRetrofitResponseItem.item);
                    mAdapter.notifyItemChanged(indexOf);
                }, throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void acceptDeliver(Order order) {
        mCompositeDisposable.add(mApi.acceptDelivery(order.getId(), mUser.getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(orderRetrofitResponseItem -> {
                    int indexOf = mOrderList.indexOf(order);
                    mOrderList.set(indexOf, orderRetrofitResponseItem.item);
                    mAdapter.notifyItemChanged(indexOf);
                }, throwable -> DialogUtil.showErrorDialog(this, throwable)));
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText(getString(R.string.content_your_order));
    }

    @Override
    public void onReasonDialogCallback(String reason) {
        switch (mCurrentOrder.getButtonId()) {
            case R.id.btn_reject_delivery:
                rejectDeliver(mCurrentOrder, reason);
                break;
            case R.id.btn_reject_receive:
                deniedOrder(mCurrentOrder, reason);
                break;
        }
    }
}
