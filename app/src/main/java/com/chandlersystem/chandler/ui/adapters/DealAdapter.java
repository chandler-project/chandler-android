package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.retrofit.Deal;
import com.chandlersystem.chandler.databinding.ItemDealBinding;
import com.chandlersystem.chandler.databinding.ItemSelectCategoryBinding;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealHolder> {
    private static final String TAG = DealAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Deal> mDealList;

    private final PublishSubject<Deal> mDealClicks = PublishSubject.create();


    public PublishSubject<Deal> getDealClicks() {
        return mDealClicks;
    }

    public DealAdapter(Context context, List<Deal> categories) {
        this.mContext = context;
        this.mDealList = categories;
    }

    @Override
    public DealHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DealHolder(LayoutInflater.from(mContext).inflate(R.layout.item_deal, parent, false));
    }

    @Override
    public void onBindViewHolder(DealHolder holder, int position) {
        Deal deal = mDealList.get(position);

        setupViews(holder.mBinding, deal);

        // Fake -- will remote these line later
        if (position % 5 == 0) {
            ViewUtil.toggleView(holder.mBinding.layoutCategoryName.layoutCategoryName, true);
        } else {
            ViewUtil.toggleView(holder.mBinding.layoutCategoryName.layoutCategoryName, false);
        }
    }

    private void setupViews(ItemDealBinding binding, Deal deal) {
        ViewUtil.showImage(mContext, "http://lorempixel.com/50/50/sports/1/", binding.layoutProfile.ivProfile);
        ViewUtil.setText(binding.layoutProfile.tvUserName, "Serious Bee");
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, "12k");
        ViewUtil.setText(binding.tvEndDate, "20/7/2018");
        ViewUtil.showImage(mContext, "http://lorempixel.com/400/320/sports/", binding.ivProduct);
        ViewUtil.setText(binding.tvPrice, "$200");
        ViewUtil.setText(binding.tvProductTitle, "What is Lorem Ipsum?");
        ViewUtil.setText(binding.tvProductDetail, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s");
        ViewUtil.setText(binding.layoutCategoryName.tvCategoryName, "Trending");
    }

    @Override
    public int getItemCount() {
        return mDealList.size();
    }

    static class SelectCategoryHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemSelectCategoryBinding mBinding;

        public SelectCategoryHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    static class DealHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemDealBinding mBinding;

        public DealHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
