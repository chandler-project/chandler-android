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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.FragmentBidDialogBinding;
import com.chandlersystem.chandler.databinding.FragmentBuyDialogBinding;

public class BuyDialog extends DialogFragment {
    // ApiConstant
    public static final String TAG = BuyDialog.class.getSimpleName();

    // DataBinding
    private FragmentBuyDialogBinding mBinding;

    // Callback event
    private BuyDialogCallback mCallback;

    public interface BuyDialogCallback {
        void onBuyDialogResponse(int amount);
    }

    public static BuyDialog getInstance() {
        BuyDialog alertDialog = new BuyDialog();
        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BuyDialogCallback) {
            mCallback = (BuyDialogCallback) context;
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
                R.layout.fragment_buy_dialog,
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
        mBinding.layoutButtonBid.btnPrimary.setText(getString(R.string.content_buy_now));
    }

    private void handleEvents() {
        mBinding.layoutButtonBid.btnPrimary.setOnClickListener(v -> {
            mCallback.onBuyDialogResponse(Integer.parseInt(mBinding.etAmount.getText().toString()));
            dismiss();
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
