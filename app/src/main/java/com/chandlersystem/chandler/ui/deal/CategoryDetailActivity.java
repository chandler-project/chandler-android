package com.chandlersystem.chandler.ui.deal;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.custom_views.CategoryDecoration;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ActivityCategoryDetailBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.DealAdapter;
import com.chandlersystem.chandler.ui.deal_detail.DealDetailActivity;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class CategoryDetailActivity extends AppCompatActivity {
    private ActivityCategoryDetailBinding mBinding;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private DealAdapter mDealAdapter;

    private static final String ARGUMENT_CATEGORY = "argument-category";

    private Category mCategory;

    @Inject
    ChandlerApi mApi;

    public static Intent getInstance(Context context, Category category) {
        Intent i = new Intent(context, CategoryDetailActivity.class);
        i.putExtra(ARGUMENT_CATEGORY, category);
        return i;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_category_detail);

        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(this))
                .build();

        mActivityComponent.inject(this);

        mCategory = getIntent().getParcelableExtra(ARGUMENT_CATEGORY);

        setupViews();
        handleEvents();
        callApi();
    }

    private void callApi() {
        mCompositeDisposable.add(mApi.getDealListOfCategory(mCategory.getId(), UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(dealRetrofitResponseListItem -> setAdapter(dealRetrofitResponseListItem.items),
                        throwable -> DialogUtil.showErrorDialog(this, throwable)));
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

    private void setAdapter(List<Deal> dealList) {
        mDealAdapter = new DealAdapter(this, dealList, DealAdapter.DealType.DEAL_EXTRA);
        mBinding.recyclerViewDeals.setAdapter(mDealAdapter);

        clickDeal();
        clickUpvote();
        clickDownVote();
    }

    private void clickDownVote() {
        mCompositeDisposable.add(mDealAdapter.getDownvotes().subscribe(deal -> {
            mCompositeDisposable.add(
                    mApi.downVote(deal.getId(), UserManager.getUserSync().getAuthorization())
                            .compose(RxUtil.withSchedulers())
                            .subscribe(retrofitResponseItem -> {
                            }, Throwable::printStackTrace));
        }, Throwable::printStackTrace));
    }

    private void clickUpvote() {
        mCompositeDisposable.add(mDealAdapter.getUpvotes().subscribe(deal -> {
            mCompositeDisposable.add(
                    mApi.upVote(deal.getId(), UserManager.getUserSync().getAuthorization())
                            .compose(RxUtil.withSchedulers())
                            .subscribe(retrofitResponseItem -> {
                            }, Throwable::printStackTrace));
        }, Throwable::printStackTrace));
    }

    private void clickDeal() {
        mCompositeDisposable.add(mDealAdapter.getDealClicks()
                .subscribe(this::startDealDetailActivity, Throwable::printStackTrace));
    }

    private void startDealDetailActivity(Deal deal) {
        startActivity(DealDetailActivity.getInstance(this, deal));
    }

    private void setupToolbar() {
        mBinding.layoutToolbar.tvTitle.setText(mCategory.getName());
    }
}
