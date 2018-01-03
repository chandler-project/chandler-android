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
import com.chandlersystem.chandler.databinding.FragmentSelectDateBinding;
import com.chandlersystem.chandler.databinding.FragmentSelectItemBinding;
import com.chandlersystem.chandler.ui.adapters.SelectDateAdapter;
import com.chandlersystem.chandler.utilities.ViewUtil;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectDateFragment extends Fragment {
    private static final String ARGUMENT_TITLE = "argument-title";

    private FragmentSelectDateBinding mBinding;

    private SelectDateAdapter mCategoryAdapter;

    private SelectDateListener mListener;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private String mTitle;

    public interface SelectDateListener {
        void onDateSelected(String date);
    }

    public SelectDateFragment() {
        // Required empty public constructor
    }

    public static SelectDateFragment getInstance(String title) {
        SelectDateFragment fragment = new SelectDateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_TITLE, title);
        fragment.setArguments(bundle);
        return new SelectDateFragment();
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
        if (context instanceof SelectDateListener) {
            mListener = (SelectDateListener) context;
        } else {
            throw new RuntimeException("Please make the context implement SelectDateListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_date, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            mTitle = getArguments().getString(ARGUMENT_TITLE);
        }

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
                .subscribe(category -> mListener.onDateSelected(category), Throwable::printStackTrace));
    }

    private void setupViews() {
        setupTitle();
        setupRecyclerView();
    }

    private void setupTitle() {
        ViewUtil.setText(mBinding.tvTitle, mTitle);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getContext());
        mCategoryAdapter = new SelectDateAdapter(getContext());
        mBinding.rvItems.setLayoutManager(mLayoutManger);
        mBinding.rvItems.setNestedScrollingEnabled(false);
        mBinding.rvItems.setHasFixedSize(true);
    }
}
