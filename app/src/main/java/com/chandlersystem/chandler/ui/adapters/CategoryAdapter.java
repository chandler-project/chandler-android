package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.databinding.ItemCategoryDealBinding;
import com.chandlersystem.chandler.databinding.ItemSelectCategoryBinding;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class CategoryAdapter extends RecyclerView.Adapter {
    public enum CategoryType {
        // Use in @SelectCategoryActivity
        SELECT_CATEGORY_ACTIVITY,
        // Use in @DealFragment at center view
        DEAL_FRAGMENT_CENTER,
        // Use in @DealFragment at @CollapsingToolBarLayout
        DEAL_FRAGMENT_TOP
    }


    private static final String TAG = CategoryAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Category> mCategories;

    private CategoryType mCategoryType;

    private final PublishSubject<Integer> mSelectedCategoryChanged = PublishSubject.create();

    private final PublishSubject<Category> mCategoryClicks = PublishSubject.create();

    private int mTotalSelectedCategory = 0;

    public PublishSubject<Integer> getCategoryChangedListener() {
        return mSelectedCategoryChanged;
    }

    public PublishSubject<Category> getmCategoryClicks() {
        return mCategoryClicks;
    }

    public CategoryAdapter(Context context, List<Category> categories, CategoryType type) {
        this.mContext = context;
        this.mCategories = categories;
        this.mCategoryType = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (mCategoryType) {
            case SELECT_CATEGORY_ACTIVITY:
                return new SelectCategoryHolder(LayoutInflater.from(mContext).inflate(R.layout.item_select_category, parent, false));
            case DEAL_FRAGMENT_CENTER:
                return new DealCategoryHolder(LayoutInflater.from(mContext).inflate(R.layout.item_category_deal, parent, false));
            case DEAL_FRAGMENT_TOP:
                return null;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Category category = mCategories.get(position);

        switch (mCategoryType) {
            case SELECT_CATEGORY_ACTIVITY:
                setupView((SelectCategoryHolder) holder, category);
                break;
            case DEAL_FRAGMENT_CENTER:
                setupView((DealCategoryHolder) holder, category);
                break;
            case DEAL_FRAGMENT_TOP:
                break;
            default:
                break;
        }
    }

    private void setupView(SelectCategoryHolder holder, Category category) {
        ViewUtil.showImage(mContext, category.getImageUrl(), holder.mBinding.ivCategory);
        holder.mBinding.tvCategory.setText(category.getName());

        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = RxView.clicks(holder.itemView)
                .compose(RxUtil.withShortThrottleFirst())
                .subscribe(o -> {
                    if (category.isSelected) {
                        ViewUtil.toggleView(holder.mBinding.ivCheck, false);
                        changeTotalSelectedCategory(false);
                    } else {
                        ViewUtil.toggleView(holder.mBinding.ivCheck, true);
                        changeTotalSelectedCategory(true);
                    }
                    category.toggleSelectedValue();
                    mSelectedCategoryChanged.onNext(mTotalSelectedCategory);
                });
    }

    private void setupView(DealCategoryHolder holder, Category category) {
        ViewUtil.showImage(mContext, category.getImageUrl(), holder.mBinding.ivCategory);
        holder.mBinding.tvCategory.setText(category.getName());

        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = RxView.clicks(holder.itemView)
                .compose(RxUtil.withShortThrottleFirst())
                .subscribe(o -> mCategoryClicks.onNext(category));
    }

    private void changeTotalSelectedCategory(boolean increase) {
        mTotalSelectedCategory += increase ? 1 : -1;
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    static class SelectCategoryHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemSelectCategoryBinding mBinding;

        public SelectCategoryHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    static class DealCategoryHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemCategoryDealBinding mBinding;

        public DealCategoryHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
