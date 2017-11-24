package com.chandlersystem.chandler.ui.createdeal;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityCreateDealBinding;

public class CreateDealActivity extends AppCompatActivity {

    private ActivityCreateDealBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_deal);
        mBinding.toolbar.setTitle("Create new hot deal");
        setSupportActionBar(mBinding.toolbar);

        mBinding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
