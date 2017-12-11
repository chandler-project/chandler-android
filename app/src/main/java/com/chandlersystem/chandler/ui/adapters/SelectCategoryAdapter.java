package com.chandlersystem.chandler.ui.adapters;

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

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.InsertCategoryHolder> {
    private static final String TAG = SelectCategoryAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Category> mCategories;


    private final PublishSubject<Category> mCategoryClicks = PublishSubject.create();

    public PublishSubject<Category> getCategoryClicks() {
        return mCategoryClicks;
    }

    public SelectCategoryAdapter(Context context, List<Category> categories) {
        this.mContext = context;
        this.mCategories = categories;
    }

    @Override
    public InsertCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new InsertCategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(InsertCategoryHolder holder, int position) {
        Category category = mCategories.get(position);
        setupView(holder, category);
    }

    private void setupView(InsertCategoryHolder holder, Category category) {
        ViewUtil.setText(holder.mBinding.tvCategoryName, category.getName());
        ViewUtil.setText(holder.mBinding.tvSubCategory, category.getSubCategory());

        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = RxView.clicks(holder.itemView)
                .compose(RxUtil.withShortThrottleFirst())
                .subscribe(o -> mCategoryClicks.onNext(category));
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    static class InsertCategoryHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemCategoryBinding mBinding;

        public InsertCategoryHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
