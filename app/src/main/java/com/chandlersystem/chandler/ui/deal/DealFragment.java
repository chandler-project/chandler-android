package com.chandlersystem.chandler.ui.deal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.custom_views.LinearItemDecoration;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.data.models.retrofit.Deal;
import com.chandlersystem.chandler.databinding.FragmentDealBinding;
import com.chandlersystem.chandler.ui.adapters.DealAdapter;
import com.chandlersystem.chandler.ui.adapters.ImagePagerAdapter;
import com.chandlersystem.chandler.ui.adapters.CategoryAdapter;
import com.chandlersystem.chandler.ui.deal_detail.DealDetailActivity;
import com.chandlersystem.chandler.ui.main.MainActivity;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class DealFragment extends Fragment {
    private FragmentDealBinding mBinding;

    private final List<String> mFeaturedDealImgUrl = new ArrayList<>();

    private ImagePagerAdapter mImagePagerAdapter;

    private CategoryAdapter mCategoryAdapter;

    private CategoryAdapter mSmallCategoryAdapter;

    private DealAdapter mDealAdapter;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    ChandlerApi mApi;

    public DealFragment() {
        // Required empty public constructor
    }

    public static DealFragment newInstance() {
        DealFragment fragment = new DealFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.getActivityComponent().inject(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_deal, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSlideShow();
        setupToolbar();
        setupCategoryRecyclerView();
        setupDealRecyclerView();
        handleEvents();
        callApi();
    }

    private void setupDealRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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
        mDealAdapter = new DealAdapter(getContext(), dealList, DealAdapter.DealType.DEAL_MAIN);
        mBinding.recyclerViewDeals.setLayoutManager(layoutManager);
        mBinding.recyclerViewDeals.setNestedScrollingEnabled(true);
        mBinding.recyclerViewDeals.setHasFixedSize(true);
        mBinding.recyclerViewDeals.addItemDecoration(new LinearItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_normal)));
        mBinding.recyclerViewDeals.setAdapter(mDealAdapter);
    }

    private void setupCategoryRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
        mBinding.recyclerViewCatgories.setLayoutManager(layoutManager);
        mBinding.recyclerViewCatgories.setNestedScrollingEnabled(false);
        mBinding.recyclerViewCatgories.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.recyclerViewSmallCategory.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewSmallCategory.setNestedScrollingEnabled(false);
        mBinding.recyclerViewSmallCategory.setHasFixedSize(true);
    }

    private void callApiGetCategory() {
        mCompositeDisposable.add(mApi.getCategoryList()
                .compose(RxUtil.withSchedulers())
                .map(categoryRetrofitResponseListItem -> categoryRetrofitResponseListItem.items)
                .subscribe(this::setCategoryAdapter, Throwable::printStackTrace));
    }

    private void setCategoryAdapter(List<Category> categoryList) {
        mCategoryAdapter = new CategoryAdapter(getContext(), categoryList, CategoryAdapter.CategoryType.DEAL_FRAGMENT_CENTER);
        mSmallCategoryAdapter = new CategoryAdapter(getContext(), categoryList, CategoryAdapter.CategoryType.DEAL_FRAGMENT_TOP);

        mBinding.recyclerViewCatgories.setAdapter(mCategoryAdapter);
        mBinding.recyclerViewSmallCategory.setAdapter(mSmallCategoryAdapter);

        bigCategoryItemClicks();
        smallCategoryItemClicks();
    }

    private void smallCategoryItemClicks() {
        mCompositeDisposable.add(mSmallCategoryAdapter.getCategoryClicks()
                .subscribe(category -> startCategoryDetailActivity(), Throwable::printStackTrace));
    }

    private void startCategoryDetailActivity() {
        startActivity(CategoryDetailActivity.getInstance(getContext()));
    }

    private void bigCategoryItemClicks() {
        mCompositeDisposable.add(mCategoryAdapter.getCategoryClicks()
                .subscribe(category -> startCategoryDetailActivity(), Throwable::printStackTrace));
    }

    private void setupToolbar() {

    }

    private void setupSlideShow() {
        if (getContext() == null) {
            return;
        }

        mFeaturedDealImgUrl.add("http://lorempixel.com/400/200/sports/1/");
        mFeaturedDealImgUrl.add("http://lorempixel.com/400/200/sports/2/");
        mFeaturedDealImgUrl.add("http://lorempixel.com/400/200/sports/3/");
        mFeaturedDealImgUrl.add("http://lorempixel.com/400/200/sports/4/");
        mFeaturedDealImgUrl.add("http://lorempixel.com/400/200/sports/5/");
        mFeaturedDealImgUrl.add("http://lorempixel.com/400/200/sports/6/");
        mImagePagerAdapter = new ImagePagerAdapter(getContext(), mFeaturedDealImgUrl);
        mBinding.viewpagerSlideShow.setAdapter(mImagePagerAdapter);
        mBinding.viewpagerSlideShow.setOffscreenPageLimit(3);
        mBinding.indicator.setViewPager(mBinding.viewpagerSlideShow);
    }

    private void handleEvents() {
        swipeToRefreshEvent();
        appbarCollapse();
        clickDeal();
    }

    private void callApi() {
        callApiGetCategory();
    }

    private void clickDeal() {
        mDealAdapter.getDealClicks().subscribe(this::startDealDetailActivity, Throwable::printStackTrace);
    }

    private void appbarCollapse() {
        mBinding.appbar.addOnOffsetChangedListener((appBarLayout, offset) -> {
            boolean isCollapsingToolbarCollapsed = Math.abs(offset) == appBarLayout.getTotalScrollRange();
            if (isCollapsingToolbarCollapsed) {
                mBinding.toolbar.setVisibility(View.VISIBLE);
            } else {
                mBinding.toolbar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void swipeToRefreshEvent() {
        mCompositeDisposable.add(RxSwipeRefreshLayout.refreshes(mBinding.layoutSwipe)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> mBinding.layoutSwipe.setRefreshing(false))
                .subscribe(o -> callApi(), Throwable::printStackTrace));
    }


    private void startDealDetailActivity(Deal deal) {
        Intent i = DealDetailActivity.getInstance(getContext());
        startActivity(i);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
