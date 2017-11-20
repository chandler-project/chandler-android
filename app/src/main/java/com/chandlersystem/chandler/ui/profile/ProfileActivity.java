package com.chandlersystem.chandler.ui.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chandlersystem.chandler.GlideApp;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ActivityProfileBinding;
import com.chandlersystem.chandler.ui.profile.dummy.DummyContent;
import com.chandlersystem.chandler.utilities.CircleTransform;

public class ProfileActivity extends AppCompatActivity implements ReviewsFragment.OnReviewInteractListener{

    private ActivityProfileBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        GlideApp.with(this).load(R.drawable.avatar)
                .transform(new CircleTransform(this))
                .into(mBinding.layoutContent.imageView);

        Fragment reviewsFragment = ReviewsFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().add(R.id.review_list_container, reviewsFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Snackbar.make(mBinding.getRoot(), "Review clicked", Snackbar.LENGTH_SHORT).show();
    }
}
