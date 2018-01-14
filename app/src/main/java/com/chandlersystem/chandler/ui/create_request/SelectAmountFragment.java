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
import com.chandlersystem.chandler.ui.adapters.SelectAmountAdapter;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectAmountFragment extends Fragment {

    private static final String TAG = SelectAmountFragment.class.getCanonicalName();

    private FragmentSelectItemBinding mBinding;

    private SelectAmountAdapter mAmountAdapter;

    private SelectAmountListener mListener;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public interface SelectAmountListener {
        void onAmountSelected(String price);
    }

    public SelectAmountFragment() {
        // Required empty public constructor
    }

    public static SelectAmountFragment getInstance() {
        return new SelectAmountFragment();
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
        if (context instanceof SelectAmountListener) {
            mListener = (SelectAmountListener) context;
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
        amountChanged();
        buttonNextClick();
    }

    private void amountChanged() {
        mCompositeDisposable.add(RxTextView.textChanges(mBinding.etAccuratePrice)
                .subscribe(charSequence -> {
                    LogUtil.logD(TAG, "Text changes");
                    int currentLength = charSequence.toString().length();
                    int currentPrice = 0;

                    if (currentLength > 0) {
                        currentPrice = Integer.valueOf(charSequence.toString());
                    }

                    if (currentLength > 0 && currentPrice > 0) {
                        enableButtonNext();
                        hideErrorString();
                    } else {
                        disableButtonNext();
                        showErrorText();
                    }
                }, Throwable::printStackTrace));
    }

    private void buttonNextClick() {
        mCompositeDisposable.add(RxView.clicks(mBinding.btnNext)
                .subscribe(o -> mListener.onAmountSelected(mBinding.etAccuratePrice.getText().toString()),
                        Throwable::printStackTrace));
    }

    private void showErrorText() {
        mBinding.tvError.setText(getString(R.string.content_please_input_valid_number));
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

    private void setAdapter() {
        mBinding.rvItems.setAdapter(mAmountAdapter);
        categoryClicks();
    }

    private void categoryClicks() {
        mCompositeDisposable.add(mAmountAdapter.getCityClicks()
                .subscribe(amount -> mListener.onAmountSelected(amount), Throwable::printStackTrace));
    }

    private void setupViews() {
        setupContent();
        setupRecyclerView();
    }

    private void setupContent() {
        mBinding.tvTitle.setText(getString(R.string.content_how_many_do_you_want_to_buy));
        mBinding.etAccuratePrice.setHint(getString(R.string.content_hint_amount));
        ViewUtil.toggleView(mBinding.tvTitle, true);
        ViewUtil.toggleView(mBinding.etCurrency, false);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getContext());
        mAmountAdapter = new SelectAmountAdapter(getContext());
        mBinding.rvItems.setLayoutManager(mLayoutManger);
        mBinding.rvItems.setNestedScrollingEnabled(false);
        mBinding.rvItems.setHasFixedSize(true);
    }
}
