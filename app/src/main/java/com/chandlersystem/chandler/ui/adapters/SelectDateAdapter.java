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

public class SelectDateAdapter extends RecyclerView.Adapter<SelectDateAdapter.InsertCategoryHolder> {
    private static final String TAG = SelectDateAdapter.class.getCanonicalName();

    private Context mContext;

    private List<String> mDateList;

    private final PublishSubject<String> getDateClick = PublishSubject.create();

    public PublishSubject<String> getCategoryClicks() {
        return getDateClick;
    }

    public SelectDateAdapter(Context context) {
        this.mContext = context;
        mDateList = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.shipping_time)));
    }

    @Override
    public InsertCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new InsertCategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(InsertCategoryHolder holder, int position) {
        String date = mDateList.get(position);
        setupView(holder, date);
    }

    private void setupView(InsertCategoryHolder holder, String date) {
        ViewUtil.setText(holder.mBinding.tvCategoryName, date);

        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = RxView.clicks(holder.itemView)
                .compose(RxUtil.withShortThrottleFirst())
                .subscribe(o -> getDateClick.onNext(date));
    }

    @Override
    public int getItemCount() {
        return mDateList.size();
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
