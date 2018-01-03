package com.chandlersystem.chandler.ui.create_request;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.FragmentSelectItemBinding;
import com.chandlersystem.chandler.ui.adapters.SelectPriceAdapter;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectPriceFragment extends Fragment {

    private static final String ARGUMENT_TITLE = "argument-title";
    private static final String ARGUMENT_TYPE = "argument-type";

    public static final int TYPE_DEAL = 0;
    public static final int TYPE_REQUEST = 1;

    private static final String TAG = SelectPriceFragment.class.getCanonicalName();

    private FragmentSelectItemBinding mBinding;

    private SelectPriceAdapter mPriceAdapter;

    private SelectPriceListener mListener;

    private String mTittle;

    private List<String> mDataset;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public interface SelectPriceListener {
        void onPriceInserted(String price);
    }

    public SelectPriceFragment() {
        // Required empty public constructor
    }

    public static SelectPriceFragment getInstance(String title, int type) {
        SelectPriceFragment fragment = new SelectPriceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_TITLE, title);
        bundle.putInt(ARGUMENT_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
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
        if (context instanceof SelectPriceListener) {
            mListener = (SelectPriceListener) context;
        } else {
            throw new RuntimeException("Please make the context implement SelectPriceListener");
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
        if (getArguments() != null) {
            mTittle = getArguments().getString(ARGUMENT_TITLE);
            int type = getArguments().getInt(ARGUMENT_TYPE);
            if (type == TYPE_DEAL) {
                mDataset = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.shipping_price)));
            } else {
                mDataset = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.budget)));
            }
        }

        setupViews();
        setAdapter();
        handleEvents();
    }

    private void handleEvents() {
        buttonNextClick();
        otherPriceChange();
    }

    private void setAdapter() {
        mBinding.rvItems.setAdapter(mPriceAdapter);
        categoryClicks();
    }

    private void categoryClicks() {
        mCompositeDisposable.add(mPriceAdapter.getCategoryClicks()
                .subscribe(category -> mListener.onPriceInserted(category), Throwable::printStackTrace));
    }

    private void buttonNextClick() {
        mCompositeDisposable.add(RxView.clicks(mBinding.btnNext)
                .subscribe(o -> mListener.onPriceInserted(mBinding.etAccuratePrice.getText().toString()),
                        Throwable::printStackTrace));
    }

    private void otherPriceChange() {
        mCompositeDisposable.add(RxTextView.textChanges(mBinding.etAccuratePrice)
                .subscribe(charSequence -> {
                    LogUtil.logD(TAG, "Text changes");
                    int currentLength = charSequence.toString().length();
                    int currentPrice = 0;

                    if (currentLength > 0) {
                        currentPrice = Integer.valueOf(charSequence.toString());
                    }

                    if (currentLength > 0 && currentPrice > 1000 && currentPrice < 99999999) {
                        enableButtonNext();
                        hideErrorString();
                    } else {
                        disableButtonNext();
                        showErrorText();
                    }
                }, Throwable::printStackTrace));
    }

    private void showErrorText() {
        mBinding.tvError.setText(getString(R.string.content_please_input_a_bigger_value));
        ViewUtil.toggleView(mBinding.tvError, true);
    }

    private void hideErrorString() {
        ViewUtil.toggleView(mBinding.tvError, false);
    }

    private void disableButtonNext() {
        mBinding.btnNext.setEnabled(false);
        mBinding.btnNext.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorInactive));
    }

    private void enableButtonNext() {
        mBinding.btnNext.setEnabled(true);
        mBinding.btnNext.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
    }

    private void setupViews() {
        setupTitle();
        setupRecyclerView();
    }

    private void setupTitle() {
        ViewUtil.setText(mBinding.tvTitle, mTittle);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getContext());
        mPriceAdapter = new SelectPriceAdapter(getContext(), mDataset);
        mBinding.rvItems.setLayoutManager(mLayoutManger);
        mBinding.rvItems.setNestedScrollingEnabled(false);
        mBinding.rvItems.setHasFixedSize(true);
    }
}
