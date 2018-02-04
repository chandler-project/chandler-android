package com.chandlersystem.chandler.ui.cart;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.Order;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityCartBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.CartAdapter;
import com.chandlersystem.chandler.ui.payment.PaymentActivity;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.TextUtil;
import com.jakewharton.rxbinding2.view.RxView;

import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding mBinding;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private CartAdapter mAdapter;

    private List<Order> mOrderList;

    private double mTotalAmount;

    @Inject
    ChandlerApi mApi;

    public static Intent getInstance(Context context) {
        return new Intent(context, CartActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cart);

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
        mCompositeDisposable.add(mApi.getPendingOrder(UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(retrofitResponseListItem -> setAdapter(retrofitResponseListItem.items), Throwable::printStackTrace));
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));

        mCompositeDisposable.add(buttonPaymentClicks()
                .subscribe(o -> paymentApi(), Throwable::printStackTrace));
    }

    private void paymentApi() {
        startPaymentActivity();
    }

    private void startPaymentActivity() {
        startActivity(PaymentActivity.getInstance(this));
    }

    private Observable<Object> buttonPaymentClicks() {
        return RxView.clicks(mBinding.btnBuy);
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }

    private void setupViews() {
        setupToolbar();
        setupCartRecyclerView();
        setupButtonPayment();
    }

    private void setupButtonPayment() {
        mBinding.btnBuy.setText(getString(R.string.make_a_payment));
    }

    private void setupCartRecyclerView() {
        mBinding.rvCart.setHasFixedSize(true);
        mBinding.rvCart.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setAdapter(List<Order> orderList) {
        mOrderList = orderList;
        mAdapter = new CartAdapter(this, mOrderList, UserManager.getUserSync().getId());
        mBinding.rvCart.setAdapter(mAdapter);

        setTotalAmount();
        removeOrderClicks();
    }

    private void setTotalAmount() {
        final String paymentText = getString(R.string.content_buy_now) + " | " + TextUtil.formatCurrency(getTotal()) + "VND";
        mBinding.btnBuy.setText(paymentText);
    }

    private double getTotal() {
        if (mOrderList == null) {
            return 0f;
        }

        double result = 0f;
        for (Order order : mOrderList) {
            result += ((order.getShippingPrice() + order.getProductPrice()) * order.getItem().getAmount());
        }

        return result;
    }

    private void removeOrderClicks() {
        mCompositeDisposable.add(mAdapter.getDisclaimClicks().subscribe(this::callRemoveOrderApi,
                Throwable::printStackTrace));
    }

    private void callRemoveOrderApi(Order order) {
        mApi.removeOrder(order.getId(), UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(retrofitResponseItem -> removeSuccess(), Throwable::printStackTrace);
    }

    private void removeSuccess() {
        setTotalAmount();
        Toast.makeText(this, getString(R.string.content_remove_success), Toast.LENGTH_SHORT).show();
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText(getString(R.string.content_your_cart));
    }
}
