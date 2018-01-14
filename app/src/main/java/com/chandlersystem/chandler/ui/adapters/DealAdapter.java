package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.data.models.pojo.Owner;
import com.chandlersystem.chandler.data.models.pojo.Shipper;
import com.chandlersystem.chandler.databinding.ItemDealBinding;
import com.chandlersystem.chandler.databinding.ItemDealSortByCategoryBinding;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ValidateUtil;
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
        if (deal.getProductPics() != null && !deal.getProductPics().isEmpty() && ValidateUtil.checkString(deal.getProductPics().get(0))) {
            ViewUtil.showImage(mContext, deal.getProductPics().get(0), binding.ivProduct);
            ViewUtil.toggleView(binding.ivProduct, true);
        } else {
            ViewUtil.toggleView(binding.ivProduct, false);
        }

        Shipper shipper = deal.getShipper();
        if (ValidateUtil.checkString(shipper.getAvatar())) {
            ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + shipper.getAvatar(), binding.layoutProfile.ivProfile);
        } else {
            ViewUtil.setImage(binding.layoutProfile.ivProfile, R.drawable.ic_placeholder_avatar);
        }

        ViewUtil.setText(binding.layoutProfile.tvUserName, shipper.getFullName());
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, shipper.getPoints() + shipper.getPoints() > 2 ? mContext.getString(R.string.content_points) : mContext.getString(R.string.content_point));
        ViewUtil.setText(binding.tvEndDate, deal.getShippingTime());
        ViewUtil.setText(binding.tvPrice, deal.getPrice() + deal.getCurrency());
        ViewUtil.setText(binding.tvShippingPrice, mContext.getString(R.string.content_shipping_price) + deal.getShippingPrice() + deal.getCurrency());
        ViewUtil.setText(binding.tvProductTitle, deal.getProductName());
        ViewUtil.setText(binding.tvProductDetail, deal.getProductDesc());
        ViewUtil.setText(binding.layoutCategoryName.tvCategoryName, deal.getCategory().getName());

        List<Owner> requesterList = deal.getRequesters();
        if (requesterList != null && !requesterList.isEmpty()) {
            ViewUtil.toggleView(binding.layoutManyProfile.layoutManyProfile, true);
            ViewUtil.setText(binding.layoutManyProfile.tvManyProfile, requesterList.size() + " " + mContext.getString(R.string.content_requester));

            if (requesterList.get(0) != null) {
                Owner owner0 = requesterList.get(0);
                ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + owner0.getAvatar(), binding.layoutManyProfile.ivProfile1);
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile1, true);
            } else {
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile1, false);
            }

            if (requesterList.size() >= 2) {
                Owner owner1 = requesterList.get(1);
                ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + owner1.getAvatar(), binding.layoutManyProfile.ivProfile2);
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile2, true);
            } else {
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile2, false);
            }

            if (requesterList.size() >= 3) {
                Owner owner2 = requesterList.get(2);
                ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + owner2.getAvatar(), binding.layoutManyProfile.ivProfile3);
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile3, true);
            } else {
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile3, false);
            }

        } else {
            ViewUtil.toggleView(binding.layoutManyProfile.layoutManyProfile, false);
        }
    }

    private void setupViews(ItemDealSortByCategoryBinding binding, Deal deal) {
        if (deal.getProductPics() != null && !deal.getProductPics().isEmpty() && ValidateUtil.checkString(deal.getProductPics().get(0))) {
            ViewUtil.showImage(mContext, deal.getProductPics().get(0), binding.ivProduct);
            ViewUtil.toggleView(binding.ivProduct, true);
        } else {
            ViewUtil.toggleView(binding.ivProduct, false);
        }

        Shipper shipper = deal.getShipper();
        if (ValidateUtil.checkString(shipper.getAvatar())) {
            ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + shipper.getAvatar(), binding.layoutProfile.ivProfile);
        } else {
            ViewUtil.setImage(binding.layoutProfile.ivProfile, R.drawable.ic_placeholder_avatar);
        }

        ViewUtil.setText(binding.layoutProfile.tvUserName, shipper.getFullName());
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, shipper.getPoints() + shipper.getPoints() > 2 ? mContext.getString(R.string.content_points) : mContext.getString(R.string.content_point));
        ViewUtil.setText(binding.tvEndDate, deal.getShippingTime());
        ViewUtil.setText(binding.tvPrice, deal.getPrice() + deal.getCurrency());
        ViewUtil.setText(binding.tvProductTitle, deal.getProductName());
        ViewUtil.setText(binding.tvProductDetail, deal.getProductDesc());
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
