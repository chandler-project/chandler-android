package com.chandlersystem.chandler.ui.become_shipper;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityBecomeShipperBinding;
import com.chandlersystem.chandler.utilities.ViewUtil;

public class BecomeShipperActivity extends AppCompatActivity {
    private ActivityBecomeShipperBinding mBinding;

    public static Intent getIntent(Context context) {
        return new Intent(context, BecomeShipperActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_become_shipper);
        setupViews();
        handleEvents();
    }

    private void handleEvents() {

    }

    private void setupViews() {
        ViewUtil.showImage(this, "https://media.giphy.com/media/l1BgSKQQgjBO80I5q/giphy.gif", mBinding.ivOnboardingImage);
        mBinding.tvTitle.setText(getString(R.string.content_want_to_become_a_shipper));
        mBinding.tvSubTitle.setText(Html.fromHtml(getString(R.string.content_please_read_our_term_and_condition)));
        mBinding.btnBecomeShipper.setText(getString(R.string.content_become_a_shipper));
    }
}
