package com.chandlersystem.chandler.ui.create_request;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.databinding.FragmentSelectItemBinding;
import com.chandlersystem.chandler.ui.adapters.SelectCategoryAdapter;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCategoryFragment extends Fragment {
    private FragmentSelectItemBinding mBinding;

    private SelectCategoryAdapter mCategoryAdapter;

    private SelectCategoryListener mListener;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public interface SelectCategoryListener {
        void onCategorySelected(Category category);
    }

    public SelectCategoryFragment() {
        // Required empty public constructor
    }

    public static SelectCategoryFragment getInstance() {
        return new SelectCategoryFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mCompositeDisposable.clear();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectCategoryListener) {
            mListener = (SelectCategoryListener) context;
        } else {
            throw new RuntimeException("Please make the context implement SelectCategoryListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_item, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        handleEvents();
    }

    private void handleEvents() {
        setAdapter();
    }

    private void setAdapter() {
        mBinding.rvItems.setAdapter(mCategoryAdapter);
        categoryClicks();
    }

    private void categoryClicks() {
        mCompositeDisposable.add(mCategoryAdapter.getCategoryClicks()
                .subscribe(category -> mListener.onCategorySelected(category), Throwable::printStackTrace));
    }

    private void setupViews() {
        setupTitle();
        setupRecyclerView();
    }

    private void setupTitle() {
        mBinding.tvTitle.setText(getString(R.string.content_it_is_free_to_post_deal));
        ViewUtil.toggleView(mBinding.tvTitle, true);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getContext());
        List<Category> categoryList = new ArrayList<>();
        String url = "https://cnet1.cbsistatic.com/img/9vw_9Ye3lmgdrRm-sUGUXsi0YxU=/300x250/2017/07/13/17f0d405-2f43-4b1e-9972-18f3910363bd/focal-listen-wireless-07.jpg";
        String subCategory = "Ti vi, tu lanh, may giat, laptop";
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));
        categoryList.add(new Category(url, "Lorem ipsum", subCategory));

        mCategoryAdapter = new SelectCategoryAdapter(getContext(), categoryList);
        mBinding.rvItems.setLayoutManager(mLayoutManger);
        mBinding.rvItems.setNestedScrollingEnabled(false);
        mBinding.rvItems.setHasFixedSize(true);
    }
}
