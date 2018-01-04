package com.chandlersystem.chandler.ui.deal;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.custom_views.CategoryDecoration;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.databinding.ActivityCategoryDetailBinding;
import com.chandlersystem.chandler.ui.adapters.DealAdapter;
import com.chandlersystem.chandler.ui.deal_detail.DealDetailActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class CategoryDetailActivity extends AppCompatActivity {
    private ActivityCategoryDetailBinding mBinding;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private DealAdapter mDealAdapter;

    public static Intent getInstance(Context context) {
        return new Intent(context, CategoryDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_category_detail);
        setupViews();
        setAdapter();
        handleEvents();
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonBackClicks()
                .subscribe(o -> finish(), Throwable::printStackTrace));
    }

    private Observable<Object> buttonBackClicks() {
        return RxView.clicks(mBinding.layoutToolbar.ivBack);
    }

    private void setupViews() {
        setupToolbar();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mBinding.recyclerViewDeals.setLayoutManager(gridLayoutManager);
        mBinding.recyclerViewDeals.setNestedScrollingEnabled(true);
        mBinding.recyclerViewDeals.setHasFixedSize(true);
        int categoryItemSpacing = getResources().getDimensionPixelSize(R.dimen.spacing_smaller);
        mBinding.recyclerViewDeals.addItemDecoration(new CategoryDecoration(categoryItemSpacing, 2));
    }

    private void setAdapter() {
        List<Deal> dealList = new ArrayList<>();
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        mDealAdapter = new DealAdapter(this, dealList, DealAdapter.DealType.DEAL_EXTRA);
        mBinding.recyclerViewDeals.setAdapter(mDealAdapter);

        dealClicks();
    }

    private void dealClicks() {
        mCompositeDisposable.add(mDealAdapter.getDealClicks()
                .subscribe(deal -> startDealDetailActivity(), Throwable::printStackTrace));
    }

    private void startDealDetailActivity() {
        startActivity(DealDetailActivity.getInstance(this));
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText("This is category name");
    }
}
