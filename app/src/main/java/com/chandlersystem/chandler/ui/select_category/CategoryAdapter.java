package com.chandlersystem.chandler.ui.select_category;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.databinding.ItemCategoryBinding;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private static final String TAG = CategoryAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Category> mCategories;

    private final PublishSubject<Integer> mSelectedCategoryChanged = PublishSubject.create();

    private int mTotalSelectedCategory = 0;

    public PublishSubject<Integer> getCategoryChangedListener() {
        return mSelectedCategoryChanged;
    }

    public CategoryAdapter(Context context, List<Category> categories) {
        this.mContext = context;
        this.mCategories = categories;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new CategoryHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        Category category = mCategories.get(position);
        ViewUtil.showImage(mContext, category.getImageUrl(), holder.mBinding.ivCategory);
        holder.mBinding.tvCategory.setText(category.getName());
        handleEvents(holder, category);
    }

    private void handleEvents(CategoryHolder holder, Category category) {
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

    private void changeTotalSelectedCategory(boolean increase) {
        mTotalSelectedCategory += increase ? 1 : -1;
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    static class CategoryHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemCategoryBinding mBinding;

        public CategoryHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
