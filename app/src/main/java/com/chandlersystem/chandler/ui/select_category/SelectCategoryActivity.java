package com.chandlersystem.chandler.ui.select_category;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.custom_views.CategoryDecoration;
import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.databinding.ActivitySelectCategoryBinding;
import com.chandlersystem.chandler.ui.adapters.CategoryAdapter;
import com.chandlersystem.chandler.ui.main.MainActivity;
import com.chandlersystem.chandler.utilities.LogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class SelectCategoryActivity extends AppCompatActivity {
    private static final String TAG = SelectCategoryActivity.class.getCanonicalName();

    private ActivitySelectCategoryBinding mBinding;

    private CategoryAdapter mCategoryAdapter;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static Intent getInstance(Context context) {
        return new Intent(context, SelectCategoryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_category);
        setupViews();
        handleEvents();
    }

    private void handleEvents() {
        mCompositeDisposable.add(RxView.clicks(mBinding.btnNavigate)
                .compose(RxUtil.withLongThrottleFirst())
                .subscribe(o -> startMainActivity()));
        mCompositeDisposable.add(mCategoryAdapter.getCategoryChangedListener()
                .subscribe(totalSelectedCategory -> {
                    LogUtil.logD(TAG, "Total selected category: " + totalSelectedCategory);
                    boolean isEnoughCategory = totalSelectedCategory >= 3;
                    ViewUtil.toggleView(mBinding.layoutSelected, isEnoughCategory);
                    ViewUtil.toggleView(mBinding.tvUnselected, !isEnoughCategory);
                }, Throwable::printStackTrace));
    }

    private void startMainActivity() {
        Intent intent = MainActivity.getInstance(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setupViews() {
        mBinding.layoutBranding.tvTitle.setText(getString(R.string.content_we_learn_what_you_like));
        ViewUtil.toggleView(mBinding.layoutBranding.tvTitle, true);

        RecyclerView.LayoutManager mLayoutManger = new GridLayoutManager(this, 3);
        List<Category> categoryList = new ArrayList<>();
        String url = "https://cnet1.cbsistatic.com/img/9vw_9Ye3lmgdrRm-sUGUXsi0YxU=/300x250/2017/07/13/17f0d405-2f43-4b1e-9972-18f3910363bd/focal-listen-wireless-07.jpg";
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        categoryList.add(new Category(url, "Lorem ipsum"));
        mCategoryAdapter = new CategoryAdapter(this, categoryList, CategoryAdapter.CategoryType.SELECT_CATEGORY_ACTIVITY);
        mBinding.recyclerViewCategories.setLayoutManager(mLayoutManger);
        mBinding.recyclerViewCategories.setNestedScrollingEnabled(false);
        mBinding.recyclerViewCategories.setHasFixedSize(true);
        int categoryItemSpacing = getResources().getDimensionPixelSize(R.dimen.spacing_normal);
        mBinding.recyclerViewCategories.addItemDecoration(new CategoryDecoration(categoryItemSpacing, 3));
        mBinding.recyclerViewCategories.setAdapter(mCategoryAdapter);
    }
}
