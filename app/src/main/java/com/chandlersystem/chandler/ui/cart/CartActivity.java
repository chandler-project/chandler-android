package com.chandlersystem.chandler.ui.cart;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.databinding.ActivityCartBinding;
import com.chandlersystem.chandler.ui.adapters.CartAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding mBinding;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private CartAdapter mAdapter;

    private List<Deal> mDealList;

    public static Intent getInstance(Context context) {
        return new Intent(context, CartActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        setupViews();
        handleEvents();
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));

        mCompositeDisposable.add(buttonPaymentClicks()
                .subscribe(o -> Toast.makeText(CartActivity.this, "Go to payment page", Toast.LENGTH_SHORT).show(), Throwable::printStackTrace));
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
        mDealList = new ArrayList<>();
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());
        mDealList.add(new Deal());


        mAdapter = new CartAdapter(this, mDealList);
        mBinding.rvCart.setHasFixedSize(true);
        mBinding.rvCart.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvCart.setAdapter(mAdapter);
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText(getString(R.string.content_your_cart));
    }
}
