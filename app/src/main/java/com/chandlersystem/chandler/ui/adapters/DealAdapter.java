package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.databinding.ItemDealBinding;
import com.chandlersystem.chandler.databinding.ItemDealSortByCategoryBinding;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class DealAdapter extends RecyclerView.Adapter {
    private static final String TAG = DealAdapter.class.getCanonicalName();

    public enum DealType {
        DEAL_MAIN,
        DEAL_EXTRA
    }

    private Context mContext;

    private List<Deal> mDealList;

    private DealType mDealType;

    private final PublishSubject<Deal> mDealClicks = PublishSubject.create();


    public PublishSubject<Deal> getDealClicks() {
        return mDealClicks;
    }

    public DealAdapter(Context context, List<Deal> categories, DealType dealType) {
        this.mContext = context;
        this.mDealList = categories;
        this.mDealType = dealType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mDealType == DealType.DEAL_MAIN) {
            return new MainDealHolder(LayoutInflater.from(mContext).inflate(R.layout.item_deal, parent, false));
        } else {
            return new ExtraDealHolder(LayoutInflater.from(mContext).inflate(R.layout.item_deal_sort_by_category, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Deal deal = mDealList.get(position);

        if (holder instanceof MainDealHolder) {
            MainDealHolder mainDealHolder = (MainDealHolder) holder;
            setupViews(mainDealHolder.mBinding, deal);
            clickDeal(mainDealHolder, deal);

            if (position > 0 && deal.getCategoryId().equals(mDealList.get(position - 1).getCategoryId())) {
                ViewUtil.toggleView(mainDealHolder.mBinding.layoutCategoryName.layoutCategoryName, false);
            } else {
                ViewUtil.toggleView(mainDealHolder.mBinding.layoutCategoryName.layoutCategoryName, true);
            }
        } else {
            ExtraDealHolder extraDealHolder = (ExtraDealHolder) holder;
            setupViews(extraDealHolder.mBinding, deal);
            clickDeal(extraDealHolder, deal);
        }
    }

    private void clickDeal(MainDealHolder holder, Deal deal) {
        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = RxView.clicks(holder.itemView)
                .compose(RxUtil.withLongThrottleFirst())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> mDealClicks.onNext(deal), Throwable::printStackTrace);
    }

    private void clickDeal(ExtraDealHolder holder, Deal deal) {
        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = RxView.clicks(holder.itemView)
                .compose(RxUtil.withLongThrottleFirst())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> mDealClicks.onNext(deal), Throwable::printStackTrace);
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

    private void setupViews(ItemDealSortByCategoryBinding binding, Deal deal) {
        ViewUtil.showImage(mContext, "http://lorempixel.com/50/50/sports/1/", binding.layoutProfile.ivProfile);
        ViewUtil.setText(binding.layoutProfile.tvUserName, "Serious Bee");
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, "12k");
        ViewUtil.setText(binding.tvEndDate, "20/7/2018");
        ViewUtil.showImage(mContext, "http://lorempixel.com/400/320/sports/", binding.ivProduct);
        ViewUtil.setText(binding.tvPrice, "$200");
        ViewUtil.setText(binding.tvProductTitle, "What is Lorem Ipsum?");
        ViewUtil.setText(binding.tvProductDetail, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s");
    }

    @Override
    public int getItemCount() {
        return mDealList.size();
    }

    static class MainDealHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemDealBinding mBinding;

        public MainDealHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    static class ExtraDealHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemDealSortByCategoryBinding mBinding;

        public ExtraDealHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
