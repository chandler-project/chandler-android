package com.chandlersystem.chandler.ui.deal;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.custom_views.LinearItemDecoration;
import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.data.models.retrofit.Deal;
import com.chandlersystem.chandler.databinding.FragmentDealBinding;
import com.chandlersystem.chandler.ui.adapters.DealAdapter;
import com.chandlersystem.chandler.ui.adapters.ImagePagerAdapter;
import com.chandlersystem.chandler.ui.adapters.CategoryAdapter;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class DealFragment extends Fragment {
    private FragmentDealBinding mBinding;

    private final List<String> mFeaturedDealImgUrl = new ArrayList<>();
    private ImagePagerAdapter mImagePagerAdapter;

    private CategoryAdapter mCategoryAdapter;

    private DealAdapter mDealAdapter;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

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
        mDealAdapter = new DealAdapter(getContext(), dealList);
        mBinding.recyclerViewDeals.setLayoutManager(layoutManager);
        mBinding.recyclerViewDeals.setNestedScrollingEnabled(true);
        mBinding.recyclerViewDeals.setHasFixedSize(true);
        mBinding.recyclerViewDeals.addItemDecoration(new LinearItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_small)));
        mBinding.recyclerViewDeals.setAdapter(mDealAdapter);
    }

    private void setupCategoryRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
        List<Category> categoryList = new ArrayList<>();
        String url = "https://cnet1.cbsistatic.com/img/9vw_9Ye3lmgdrRm-sUGUXsi0YxU=/300x250/2017/07/13/17f0d405-2f43-4b1e-9972-18f3910363bd/focal-listen-wireless-07.jpg";
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        mCategoryAdapter = new CategoryAdapter(getContext(), categoryList, CategoryAdapter.CategoryType.DEAL_FRAGMENT_CENTER);
        mBinding.recyclerViewCatgories.setLayoutManager(layoutManager);
        mBinding.recyclerViewCatgories.setNestedScrollingEnabled(false);
        mBinding.recyclerViewCatgories.setHasFixedSize(true);
        mBinding.recyclerViewCatgories.setAdapter(mCategoryAdapter);
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
    }

    private void appbarCollapse() {
        mBinding.appbar.addOnOffsetChangedListener((appBarLayout, offset) -> ViewUtil.toggleView(mBinding.tvTest, offset != 0));
    }

    private void swipeToRefreshEvent() {
        /*mCompositeDisposable.add(RxSwipeRefreshLayout.refreshes(mBinding.swipeRefreshLayout)
                .compose(RxUtil.withSchedulers())
                .subscribe(o -> {
                    mBinding.swipeRefreshLayout.setRefreshing(false);
                }, Throwable::printStackTrace));*/
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
