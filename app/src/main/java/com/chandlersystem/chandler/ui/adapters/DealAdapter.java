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
import com.chandlersystem.chandler.data.models.pojo.Category;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.data.models.pojo.Owner;
import com.chandlersystem.chandler.data.models.pojo.Shipper;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.ItemDealBinding;
import com.chandlersystem.chandler.databinding.ItemDealSortByCategoryBinding;
import com.chandlersystem.chandler.databinding.ItemVoteBinding;
import com.chandlersystem.chandler.ui.deal.CategoryDetailActivity;
import com.chandlersystem.chandler.ui.profile.UserProfileActivity;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.TextUtil;
import com.chandlersystem.chandler.utilities.TimeUtil;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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

    private final PublishSubject<Deal> mUpvotes = PublishSubject.create();

    private final PublishSubject<Deal> mDownvotes = PublishSubject.create();

    public PublishSubject<Deal> getUpvotes() {
        return mUpvotes;
    }

    public PublishSubject<Deal> getDownvotes() {
        return mDownvotes;
    }

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
        if (!holder.mDisposable.isDisposed()) {
            holder.mDisposable.clear();
        }

        holder.mDisposable.add(RxView.clicks(holder.itemView)
                .compose(RxUtil.withLongThrottleFirst())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> mDealClicks.onNext(deal), Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.layoutVote.layoutUpvote)
                .subscribe(o -> {
                    mUpvotes.onNext(deal);
                    upVote(holder.mBinding.layoutVote, deal);
                }, Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.layoutVote.layoutDownvote)
                .subscribe(o -> {
                    mDownvotes.onNext(deal);
                    downVote(holder.mBinding.layoutVote, deal);
                }, Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.layoutCategoryName.tvMore)
                .subscribe(o -> startCategoryDetailActivity(deal.getCategory()), Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.layoutProfile.layoutProfile)
                .subscribe(o -> {
                    mContext.startActivity(UserProfileActivity.getIntent(mContext, deal.getShipper().getId()));
                }, Throwable::printStackTrace));
    }

    private void downVote(ItemVoteBinding holder, Deal deal) {
        int currentDownvote = deal.getDownvote();
        if (deal.isDownvoted()) {
            holder.ivDownvote.setImageResource(R.drawable.ic_downvote_inactive);
            --currentDownvote;
            holder.tvDownvoteAmount.setText(currentDownvote + "");
            deal.setDownvote(currentDownvote);
            deal.setDownvoted(false);
        } else {
            holder.ivDownvote.setImageResource(R.drawable.ic_downvote_active);
            ++currentDownvote;
            holder.tvDownvoteAmount.setText(currentDownvote + "");
            deal.setDownvoted(true);
            deal.setDownvote(currentDownvote);

            int currentUpvote = deal.getUpvote();
            if (deal.isUpvoted()) {
                holder.ivUpvote.setImageResource(R.drawable.ic_upvote_inactive);
                --currentUpvote;
                holder.tvUpvoteAmount.setText(currentUpvote + "");
                deal.setUpvoted(false);
                deal.setUpvote(currentUpvote);
            }
        }
    }

    private void upVote(ItemVoteBinding holder, Deal deal) {
        int currentUpvotes = deal.getUpvote();
        if (deal.isUpvoted()) {
            holder.ivUpvote.setImageResource(R.drawable.ic_upvote_inactive);
            --currentUpvotes;
            holder.tvUpvoteAmount.setText(currentUpvotes + "");
            deal.setUpvote(currentUpvotes);
            deal.setUpvoted(false);
        } else {
            holder.ivUpvote.setImageResource(R.drawable.ic_upvote_active);
            ++currentUpvotes;
            holder.tvUpvoteAmount.setText(currentUpvotes + "");
            deal.setUpvoted(true);
            deal.setUpvote(currentUpvotes);

            int currentDownvote = deal.getDownvote();
            if (deal.isDownvoted()) {
                holder.ivDownvote.setImageResource(R.drawable.ic_downvote_inactive);
                --currentDownvote;
                holder.tvDownvoteAmount.setText(currentDownvote + "");
                deal.setDownvoted(false);
                deal.setDownvote(currentDownvote);
            }
        }
    }

    private void startCategoryDetailActivity(Category category) {
        mContext.startActivity(CategoryDetailActivity.getInstance(mContext, category));
    }

    private void clickDeal(ExtraDealHolder holder, Deal deal) {
        if (!holder.mDisposable.isDisposed()) {
            holder.mDisposable.clear();
        }

        holder.mDisposable.add(RxView.clicks(holder.itemView)
                .compose(RxUtil.withLongThrottleFirst())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> mDealClicks.onNext(deal), Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.layoutVote.layoutUpvote)
                .subscribe(o -> {
                    mUpvotes.onNext(deal);
                    upVote(holder.mBinding.layoutVote, deal);
                }, Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.layoutVote.layoutDownvote)
                .subscribe(o -> {
                    mDownvotes.onNext(deal);
                    downVote(holder.mBinding.layoutVote, deal);
                }, Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.layoutProfile.layoutProfile)
                .subscribe(o -> {
                    mContext.startActivity(UserProfileActivity.getIntent(mContext, deal.getShipper().getId()));
                }, Throwable::printStackTrace));
    }

    private void setupViews(ItemDealBinding binding, Deal deal) {
        ViewUtil.setText(binding.layoutVote.tvUpvoteAmount, String.valueOf(deal.getUpvote()));
        ViewUtil.setText(binding.layoutVote.tvDownvoteAmount, String.valueOf(deal.getDownvote()));

        String userId = UserManager.getUserSync().getId();

        if (deal.getUpVoters().contains(userId)) {
            binding.layoutVote.ivUpvote.setImageResource(R.drawable.ic_upvote_active);
            deal.setUpvoted(true);
        } else {
            deal.setUpvoted(false);
            binding.layoutVote.ivUpvote.setImageResource(R.drawable.ic_upvote_inactive);
        }

        if (deal.getDownVoters().contains(userId)) {
            deal.setDownvoted(true);
            binding.layoutVote.ivDownvote.setImageResource(R.drawable.ic_downvote_active);
        } else {
            deal.setDownvoted(false);
            binding.layoutVote.ivDownvote.setImageResource(R.drawable.ic_downvote_inactive);
        }

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
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, shipper.getPoints() + " " + (shipper.getPoints() > 2 ? mContext.getString(R.string.content_points) : mContext.getString(R.string.content_point)));
        ViewUtil.setText(binding.tvEndDate, mContext.getString(R.string.content_expire_on) + TimeUtil.convert(TimeUtil.FORMAT_WORLD_WIDE, TimeUtil.FORMAT_DATE_VN, deal.getShippingTime()));
        ViewUtil.setText(binding.tvPrice, mContext.getString(R.string.content_deal_price) + TextUtil.formatCurrency(deal.getPrice()) + deal.getCurrency());
        ViewUtil.setText(binding.tvShippingPrice, mContext.getString(R.string.content_shipping_price) + TextUtil.formatCurrency(deal.getShippingPrice()) + deal.getCurrency());
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
        ViewUtil.setText(binding.layoutVote.tvUpvoteAmount, String.valueOf(deal.getUpvote()));
        ViewUtil.setText(binding.layoutVote.tvDownvoteAmount, String.valueOf(deal.getDownvote()));

        String userId = UserManager.getUserSync().getId();

        if (deal.getUpVoters().contains(userId)) {
            binding.layoutVote.ivUpvote.setImageResource(R.drawable.ic_upvote_active);
        } else {
            binding.layoutVote.ivUpvote.setImageResource(R.drawable.ic_upvote_inactive);
        }

        if (deal.getDownVoters().contains(userId)) {
            binding.layoutVote.ivDownvote.setImageResource(R.drawable.ic_downvote_active);
        } else {
            binding.layoutVote.ivDownvote.setImageResource(R.drawable.ic_downvote_inactive);
        }

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
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, shipper.getPoints() + (shipper.getPoints() > 2 ? mContext.getString(R.string.content_points) : mContext.getString(R.string.content_point)));
        ViewUtil.setText(binding.tvEndDate, mContext.getString(R.string.content_expire_on) + TimeUtil.convert(TimeUtil.FORMAT_WORLD_WIDE, TimeUtil.FORMAT_DATE_VN, deal.getShippingTime()));
        ViewUtil.setText(binding.tvPrice, mContext.getString(R.string.content_deal_price) + TextUtil.formatCurrency(deal.getPrice()) + deal.getCurrency());
        ViewUtil.setText(binding.tvProductTitle, deal.getProductName());
        ViewUtil.setText(binding.tvProductDetail, deal.getProductDesc());
    }

    @Override
    public int getItemCount() {
        return mDealList.size();
    }

    static class MainDealHolder extends RecyclerView.ViewHolder {
        private final CompositeDisposable mDisposable = new CompositeDisposable();
        private ItemDealBinding mBinding;

        public MainDealHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    static class ExtraDealHolder extends RecyclerView.ViewHolder {
        private final CompositeDisposable mDisposable = new CompositeDisposable();
        private ItemDealSortByCategoryBinding mBinding;

        public ExtraDealHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
