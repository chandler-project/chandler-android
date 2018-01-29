package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.models.pojo.Bidder;
import com.chandlersystem.chandler.data.models.pojo.Shipper;
import com.chandlersystem.chandler.databinding.ItemBidBinding;
import com.chandlersystem.chandler.ui.profile.UserProfileActivity;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.BidHolder> {
    private static final String TAG = BidAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Bidder> mBidList;

    private boolean mIsOwner;

    private boolean mIsOrdered;

    private final PublishSubject<Bidder> mBidClick = PublishSubject.create();

    public PublishSubject<Bidder> getBidClick() {
        return mBidClick;
    }

    public BidAdapter(Context context, List<Bidder> bidList, boolean isOwner, boolean isOrdered) {
        this.mContext = context;
        this.mBidList = bidList;
        this.mIsOwner = isOwner;
        this.mIsOrdered = isOrdered;
    }

    @Override
    public BidHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BidHolder(LayoutInflater.from(mContext).inflate(R.layout.item_bid, parent, false));
    }

    @Override
    public void onBindViewHolder(BidHolder holder, int position) {
        Bidder bidder = mBidList.get(position);

        setupViews(holder.mBinding, bidder);
        handleEvents(holder, bidder);
    }

    private void handleEvents(BidHolder holder, Bidder bidder) {
        if (!holder.mDisposable.isDisposed()) {
            holder.mDisposable.clear();
        }

        holder.mDisposable.add(RxView.clicks(holder.mBinding.btnAccept)
                .subscribe(o -> mBidClick.onNext(bidder), Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.layoutProfile.layoutProfile)
                .subscribe(o -> {
                    mContext.startActivity(UserProfileActivity.getIntent(mContext, bidder.getId()));
                }, Throwable::printStackTrace));
    }

    private void setupViews(ItemBidBinding binding, Bidder bidder) {
        if (ValidateUtil.checkString(bidder.getAvatar())) {
            ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + bidder.getAvatar(), binding.layoutProfile.ivProfile);
        } else {
            ViewUtil.setImage(binding.layoutProfile.ivProfile, R.drawable.ic_placeholder_avatar);
        }

        ViewUtil.setText(binding.layoutProfile.tvUserName, bidder.getFullName());
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, bidder.getPoints() + " " + (bidder.getPoints() > 2 ? mContext.getString(R.string.content_points) : mContext.getString(R.string.content_point)));

        ViewUtil.setText(binding.tvContent, bidder.getSentence());
        ViewUtil.setText(binding.tvBidPrice, String.valueOf(bidder.getPrice()) + " VND");

        ViewUtil.toggleView(binding.btnAccept, mIsOwner);
        ViewUtil.toggleView(binding.btnAccept, !mIsOrdered);
    }

    @Override
    public int getItemCount() {
        return mBidList.size();
    }

    static class BidHolder extends RecyclerView.ViewHolder {
        private final CompositeDisposable mDisposable = new CompositeDisposable();
        private ItemBidBinding mBinding;

        public BidHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
