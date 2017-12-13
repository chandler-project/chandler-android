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
import com.chandlersystem.chandler.databinding.FragmentSelectItemBinding;
import com.chandlersystem.chandler.ui.adapters.SelectCityAdapter;
import com.chandlersystem.chandler.utilities.ViewUtil;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCityFragment extends Fragment {

    private FragmentSelectItemBinding mBinding;

    private SelectCityAdapter mCityAdapter;

    private SelectCityListener mListener;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public interface SelectCityListener {
        void onCitySelected(String price);
    }

    public SelectCityFragment() {
        // Required empty public constructor
    }

    public static SelectCityFragment getInstance() {
        return new SelectCityFragment();
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
        if (context instanceof SelectCityListener) {
            mListener = (SelectCityListener) context;
        } else {
            throw new RuntimeException("Please make the context implement SelectCityListener");
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
        mBinding.rvItems.setAdapter(mCityAdapter);
        categoryClicks();
    }

    private void categoryClicks() {
        mCompositeDisposable.add(mCityAdapter.getCityClicks()
                .subscribe(category -> mListener.onCitySelected(category), Throwable::printStackTrace));
    }

    private void setupViews() {
        setupTitle();
        setupRecyclerView();
    }

    private void setupTitle() {
        mBinding.tvTitle.setText(getString(R.string.content_which_city_do_you_live));
        ViewUtil.toggleView(mBinding.tvTitle, true);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getContext());
        mCityAdapter = new SelectCityAdapter(getContext());
        mBinding.rvItems.setLayoutManager(mLayoutManger);
        mBinding.rvItems.setNestedScrollingEnabled(false);
        mBinding.rvItems.setHasFixedSize(true);
    }
}
