package com.chandlersystem.chandler.ui.create_request;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.request.CreateDealBody;
import com.chandlersystem.chandler.data.models.request.CreateRequestBody;
import com.chandlersystem.chandler.databinding.FragmentCompleteCreateDealBinding;
import com.chandlersystem.chandler.ui.adapters.ItemImageAdapter;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteCreateDealFragment extends Fragment {
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private FragmentCompleteCreateDealBinding mBinding;

    private CompleteCreateDealListener mListener;

    private static final int REQUEST_CODE_PICK_PHOTO = 10;

    private ItemImageAdapter mAdapter;

    private CreateDealBody mCreateDealBody;

    public interface CompleteCreateDealListener {
        void onProductCreated(CreateDealBody request);
    }

    public CompleteCreateDealFragment() {
        // Required empty public constructor
    }

    public static CompleteCreateDealFragment getInstance() {
        return new CompleteCreateDealFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CompleteCreateDealListener) {
            mListener = (CompleteCreateDealListener) context;
        } else {
            throw new RuntimeException("Please make the context implement CompleteCreateDealListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mCompositeDisposable.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_complete_create_deal, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        setupImageAdapter();
        handleEvents();
    }

    private void setupImageAdapter() {
        mAdapter = new ItemImageAdapter(getContext());
        mBinding.recyclerViewItemImages.setAdapter(mAdapter);

        mAdapter.getAddImageClicks().subscribe(integer -> startImagePicker(), Throwable::printStackTrace);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mBinding != null) {
            mBinding.etName.clearFocus();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_PHOTO: {
                if (resultCode == RESULT_OK) {
                    String imagePath = data.getData().toString();
                    mAdapter.addImage(imagePath);
                }
                break;
            }
        }
    }


    private Observable<Boolean> formValidated() {
        return Observable.combineLatest(titleValidated(), linkValidated(), priceValidated(), (aBoolean, aBoolean2, aBoolean3) -> aBoolean && aBoolean2 && aBoolean3);
    }

    private Observable<Boolean> titleValidated() {
        return RxTextView.textChanges(mBinding.etName)
                .map(charSequence -> {
                    int textLength = charSequence.toString().length();
                    return textLength >= 6 && textLength <= 100;
                });
    }

    private Observable<Boolean> linkValidated() {
        return RxTextView.textChanges(mBinding.etLinkUrl)
                .map(charSequence -> {
                    int textLength = charSequence.toString().length();
                    return textLength >= 6 && textLength <= 100 && ValidateUtil.checkUrl(charSequence.toString());
                });
    }

    private Observable<Boolean> priceValidated() {
        return RxTextView.textChanges(mBinding.etNetPrice)
                .map(charSequence -> {
                    int currentLength = charSequence.toString().length();
                    int currentPrice = 0;

                    if (currentLength > 0) {
                        currentPrice = Integer.valueOf(charSequence.toString());
                    }

                    return (currentLength > 0 && currentPrice > 1000 && currentPrice < 999999999);
                });
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonNextClicks()
                .subscribe(o -> storeNameAndGoToNextPage(), Throwable::printStackTrace));

        mCompositeDisposable.add(formValidated().subscribe(aBoolean -> {
            if (aBoolean) {
                enableButtonNext();
            } else {
                disableButtonNext();
            }
        }, Throwable::printStackTrace));
    }

    private void storeNameAndGoToNextPage() {
        final String name = mBinding.etName.getText().toString();
        final String reference = mBinding.etLinkUrl.getText().toString();
        final float productPrice = Float.valueOf(mBinding.etNetPrice.getText().toString());
        final String productDescription = mBinding.etDescription.getText().toString();
        final List<String> productImageUri = mAdapter.getData();

        CreateDealBody request = new CreateDealBody();
        request.setProductName(name);
        request.setReference(reference);
        request.setPrice(productPrice);
        request.setProductDesc(productDescription);
        request.setProductPics(productImageUri);

        mListener.onProductCreated(request);
    }

    private Observable<Object> buttonNextClicks() {
        return RxView.clicks(mBinding.layoutNext.btnPrimary);
    }

    private void setupViews() {
        mBinding.layoutNext.btnPrimary.setText(getString(R.string.content_next));
        mBinding.etName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300)});
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mBinding.recyclerViewItemImages.setHasFixedSize(true);
        mBinding.recyclerViewItemImages.setLayoutManager(
                new LinearLayoutManager(getContext()));
    }

    public void startImagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_PICK_PHOTO);
    }

    private void disableButtonNext() {
        mBinding.layoutNext.btnPrimary.setEnabled(false);
        mBinding.layoutNext.btnPrimary.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorInactive));
    }

    private void enableButtonNext() {
        mBinding.layoutNext.btnPrimary.setEnabled(true);
        mBinding.layoutNext.btnPrimary.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
    }
}
