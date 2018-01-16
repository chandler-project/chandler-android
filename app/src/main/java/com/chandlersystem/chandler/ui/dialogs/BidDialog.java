package com.chandlersystem.chandler.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.request.BidRequestBody;
import com.chandlersystem.chandler.databinding.FragmentBidBinding;
import com.chandlersystem.chandler.databinding.FragmentBidDialogBinding;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class BidDialog extends DialogFragment {
    // ApiConstant
    public static final String TAG = BidDialog.class.getSimpleName();

    public static final String ARGUMENT_AMOUNT = "argument-amount";

    // DataBinding
    private FragmentBidDialogBinding mBinding;

    // Callback event
    private BidDialogCallback mCallback;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private int mAmount;

    public interface BidDialogCallback {
        void onBid(BidRequestBody bidRequestBody);
    }

    public static BidDialog getInstance(int amount) {
        BidDialog alertDialog = new BidDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGUMENT_AMOUNT, amount);
        alertDialog.setArguments(bundle);
        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BidDialogCallback) {
            mCallback = (BidDialogCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAlertDialogInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAmount = getArguments().getInt(ARGUMENT_AMOUNT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_bid_dialog,
                container,
                false
        );
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Check if input reward is null or not
        // If it's available then show all component
        setupViews();
        handleEvents();
    }

    public void setupViews() {
        mBinding.etAmount.setText(mAmount + "");
        mBinding.layoutButtonBid.btnPrimary.setText(getString(R.string.content_bid_now));
    }

    private void handleEvents() {

        mCompositeDisposable.add(formValidated()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::enableButtonBid, Throwable::printStackTrace));

        mBinding.layoutButtonBid.btnPrimary.setOnClickListener(v -> {
            BidRequestBody body = new BidRequestBody();
            body.setPrice(Float.parseFloat(mBinding.etPrice.getText().toString()));
            body.setSpend(Integer.parseInt(mBinding.etDeliveryTime.getText().toString()));
            body.setSentence(mBinding.etNote.getText().toString());
            mCallback.onBid(body);
            dismiss();
        });
    }

    private void enableButtonBid(Boolean aBoolean) {
        if (getContext() == null) {
            dismiss();
        }

        mBinding.layoutButtonBid.btnPrimary.setEnabled(aBoolean);
        if (aBoolean) {
            mBinding.layoutButtonBid.btnPrimary.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        } else {
            mBinding.layoutButtonBid.btnPrimary.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorInactive));
        }
    }

    private Observable<Boolean> formValidated() {
        return Observable.combineLatest(priceValidated(), dateValidated(), (aBoolean, aBoolean2) -> aBoolean && aBoolean2);
    }

    private Observable<Boolean> priceValidated() {
        return RxTextView.textChanges(mBinding.etPrice)
                .map(charSequence -> {
                    int currentLength = charSequence.toString().length();
                    float currentPrice = 0f;

                    if (currentLength > 0) {
                        currentPrice = Float.valueOf(charSequence.toString());
                    }

                    return (currentPrice > 1000 && currentPrice < 999999999);
                });
    }

    private Observable<Boolean> dateValidated() {
        return RxTextView.textChanges(mBinding.etDeliveryTime)
                .map(charSequence -> {
                    int currentLength = charSequence.toString().length();
                    int currentDate = 0;

                    if (currentLength > 0) {
                        currentDate = Integer.valueOf(charSequence.toString());
                    }

                    return (currentDate > 1);
                });
    }

    @Override
    public void onResume() {
        if (getDialog() != null) {
            // Store access variables for window and blank point
            Window window = getDialog().getWindow();

            // Store dimensions of the screen in `size`
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }

        // Call super onResume after sizing
        super.onResume();
    }
}
