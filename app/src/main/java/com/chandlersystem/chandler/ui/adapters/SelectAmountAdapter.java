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

public class SelectAmountAdapter extends RecyclerView.Adapter<SelectAmountAdapter.InsertCategoryHolder> {
    private static final String TAG = SelectAmountAdapter.class.getCanonicalName();

    private Context mContext;

    private List<String> mCityList;

    private final PublishSubject<String> mCityClicks = PublishSubject.create();

    public PublishSubject<String> getCityClicks() {
        return mCityClicks;
    }

    public SelectAmountAdapter(Context context) {
        this.mContext = context;
        mCityList = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.amount)));
    }

    @Override
    public InsertCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new InsertCategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(InsertCategoryHolder holder, int position) {
        String city = mCityList.get(position);
        setupView(holder, city);
    }

    private void setupView(InsertCategoryHolder holder, String city) {
        ViewUtil.setText(holder.mBinding.tvCategoryName, city);

        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = RxView.clicks(holder.itemView)
                .compose(RxUtil.withShortThrottleFirst())
                .subscribe(o -> mCityClicks.onNext(city));
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
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
