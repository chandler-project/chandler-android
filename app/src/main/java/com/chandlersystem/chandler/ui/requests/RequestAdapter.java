package com.chandlersystem.chandler.ui.requests;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.models.pojo.Bidder;
import com.chandlersystem.chandler.data.models.pojo.Owner;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.data.models.pojo.Shipper;
import com.chandlersystem.chandler.databinding.FragmentRequestItemBinding;
import com.chandlersystem.chandler.ui.profile.UserProfileActivity;
import com.chandlersystem.chandler.utilities.TextUtil;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private final List<Request> mValues;
    private Context mContext;

    private final PublishSubject<Request> mRequestClicks = PublishSubject.create();

    private final PublishSubject<Request> mRequestBidClick = PublishSubject.create();

    public PublishSubject<Request> getRequestClicks() {
        return mRequestClicks;
    }

    public PublishSubject<Request> getRequestBidClick() {
        return mRequestBidClick;
    }

    public RequestAdapter(List<Request> mValues, Context mContext) {
        this.mValues = mValues;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Request request = mValues.get(position);

        setupViews(holder.mBinding, request);
        requestClicks(holder, request);
    }

    private void requestClicks(ViewHolder holder, Request request) {
        if (!holder.mDisposable.isDisposed()) {
            holder.mDisposable.clear();
        }

        holder.mDisposable.add(RxView.clicks(holder.itemView)
                .subscribe(o -> mRequestClicks.onNext(request), Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.layoutProfile.layoutProfile)
                .subscribe(o -> {
                    mContext.startActivity(UserProfileActivity.getIntent(mContext, request.getOwner().getId()));
                }, Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.btnBid)
                .subscribe(o -> {
                    mRequestBidClick.onNext(request);
                }, Throwable::printStackTrace));
    }

    private void setupViews(FragmentRequestItemBinding binding, Request request) {
        if (request.getProductPics() != null && !request.getProductPics().isEmpty() && ValidateUtil.checkString(request.getProductPics().get(0))) {
            ViewUtil.showImage(mContext, request.getProductPics().get(0), binding.ivProduct);
            ViewUtil.toggleView(binding.ivProduct, true);
        } else {
            ViewUtil.toggleView(binding.ivProduct, false);
        }

        Owner owner = request.getOwner();
        if (ValidateUtil.checkString(owner.getAvatar())) {
            ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + owner.getAvatar(), binding.layoutProfile.ivProfile);
        } else {
            ViewUtil.setImage(binding.layoutProfile.ivProfile, R.drawable.ic_placeholder_avatar);
        }
        ViewUtil.setText(binding.layoutProfile.tvUserName, owner.getFullName());
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, owner.getPoints() + " " + (owner.getPoints() > 2 ? mContext.getString(R.string.content_points) : mContext.getString(R.string.content_point)));
        ViewUtil.setText(binding.tvPrice, TextUtil.formatCurrency(request.getPrice()) + request.getCurrency());
        ViewUtil.setText(binding.tvShippingPrice, mContext.getString(R.string.content_request) + request.getBudget().getMin() + " - " + request.getBudget().getMax() + request.getCurrency());
        ViewUtil.setText(binding.tvProductTitle, request.getProductName());
        ViewUtil.setText(binding.tvProductDetail, request.getDescription());
        ViewUtil.setText(binding.tvDate, request.getDeadline());
        ViewUtil.setText(binding.tvProductLink, request.getReference());
        ViewUtil.setText(binding.tvAddress, request.getAddress());
        ViewUtil.setText(binding.tvAmount, "x" + request.getAmount());

        List<Bidder> requesterList = request.getBidders();
        if (requesterList != null && !requesterList.isEmpty()) {
            ViewUtil.toggleView(binding.layoutManyProfile.layoutManyProfile, true);
            ViewUtil.setText(binding.layoutManyProfile.tvManyProfile, requesterList.size() + " " + mContext.getString(R.string.content_requester));

            if (requesterList.get(0) != null) {
                Bidder owner0 = requesterList.get(0);
                ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + owner0.getAvatar(), binding.layoutManyProfile.ivProfile1);
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile1, true);
            } else {
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile1, false);
            }

            if (requesterList.size() >= 2) {
                Bidder owner1 = requesterList.get(1);
                ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + owner1.getAvatar(), binding.layoutManyProfile.ivProfile2);
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile2, true);
            } else {
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile2, false);
            }

            if (requesterList.size() >= 3) {
                Bidder owner2 = requesterList.get(2);
                ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + owner2.getAvatar(), binding.layoutManyProfile.ivProfile3);
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile3, true);
            } else {
                ViewUtil.toggleView(binding.layoutManyProfile.layoutProfile3, false);
            }

        } else {
            ViewUtil.toggleView(binding.layoutManyProfile.layoutManyProfile, false);
        }

        String requestStatus = request.getStatus();
        boolean isRequestOrdered = requestStatus.equals("Ordered");
        ViewUtil.toggleView(binding.btnBid, !isRequestOrdered);
        ViewUtil.toggleView(binding.tvOrdered, isRequestOrdered);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FragmentRequestItemBinding mBinding;
        private final CompositeDisposable mDisposable = new CompositeDisposable();

        public ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
        }

    }
}
