package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ItemCategoryBinding;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class SelectPriceAdapter extends RecyclerView.Adapter<SelectPriceAdapter.InsertCategoryHolder> {
    private static final String TAG = SelectPriceAdapter.class.getCanonicalName();

    private Context mContext;

    private List<String> mPriceList;

    private final PublishSubject<String> getPriceClicks = PublishSubject.create();

    public PublishSubject<String> getCategoryClicks() {
        return getPriceClicks;
    }

    public SelectPriceAdapter(Context mContext, List<String> mPriceList) {
        this.mContext = mContext;
        this.mPriceList = mPriceList;
    }

    @Override
    public InsertCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new InsertCategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(InsertCategoryHolder holder, int position) {
        String price = mPriceList.get(position);
        setupView(holder, price);
    }

    private void setupView(InsertCategoryHolder holder, String price) {
        ViewUtil.setText(holder.mBinding.tvCategoryName, price);

        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = RxView.clicks(holder.itemView)
                .compose(RxUtil.withShortThrottleFirst())
                .subscribe(o -> getPriceClicks.onNext(price));
    }

    @Override
    public int getItemCount() {
        return mPriceList.size();
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
