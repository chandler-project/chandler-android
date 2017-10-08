package com.chandlersystem.chandler.ui.requests;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityRequestsBinding;
import com.chandlersystem.chandler.ui.requests.dummy.DummyContent;

public class RequestsActivity extends AppCompatActivity implements RequestsFragment.OnListFragmentInteractionListener{

    private ActivityRequestsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_requests);
        mBinding.toolbar.setTitle("Requests");
        setSupportActionBar(mBinding.toolbar);

        RequestsFragment fragment = RequestsFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, fragment).commit();

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
