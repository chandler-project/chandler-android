package com.chandlersystem.chandler.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
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
import com.chandlersystem.chandler.databinding.FragmentBuyDialogBinding;
import com.chandlersystem.chandler.databinding.FragmentReasonDialogBinding;

public class ReasonDialog extends DialogFragment {
    // ApiConstant
    public static final String TAG = ReasonDialog.class.getSimpleName();

    // DataBinding
    private FragmentReasonDialogBinding mBinding;

    // Callback event
    private ReasonDialogCallback mCallback;

    public interface ReasonDialogCallback {
        void onReasonDialogCallback(String reason);
    }

    public static ReasonDialog getInstance() {
        return new ReasonDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ReasonDialogCallback) {
            mCallback = (ReasonDialogCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ReasonDialogCallback");
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
                R.layout.fragment_reason_dialog,
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
    }

    private void handleEvents() {
        mBinding.btnPrimary.setOnClickListener(v -> {
            mCallback.onReasonDialogCallback(mBinding.etReason.getText().toString());
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
