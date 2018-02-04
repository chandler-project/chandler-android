package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.data.models.pojo.Order;
import com.chandlersystem.chandler.data.models.pojo.Shipper;
import com.chandlersystem.chandler.databinding.ItemCartTransactionBinding;
import com.chandlersystem.chandler.databinding.ItemOrderBinding;
import com.chandlersystem.chandler.ui.deal_detail.DealDetailActivity;
import com.chandlersystem.chandler.utilities.TextUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.CartHolder> {
    private static final String TAG = OrderAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Order> mOrderList;

    private String userId;

    private final PublishSubject<Order> mOrderClicks = PublishSubject.create();

    public PublishSubject<Order> getOrderClicks() {
        return mOrderClicks;
    }

    public OrderAdapter(Context mContext, List<Order> mOrderList, String userId) {
        this.mContext = mContext;
        this.mOrderList = mOrderList;
        this.userId = userId;
    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartHolder(LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(CartHolder holder, int position) {
        Order order = mOrderList.get(position);

        setupViews(holder.mBinding, order);
        handleEvents(holder, order);
    }

    private void handleEvents(CartHolder holder, Order order) {
        if (!holder.mDisposable.isDisposed()) {
            holder.mDisposable.clear();
        }

        holder.mDisposable.add(RxView.clicks(holder.mBinding.btnAcceptDelivery)
                .subscribe(o -> {
                    order.setButtonId(holder.mBinding.btnAcceptDelivery.getId());
                    mOrderClicks.onNext(order);
                }, Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.btnRejectDelivery)
                .subscribe(o -> {
                    order.setButtonId(holder.mBinding.btnRejectDelivery.getId());
                    mOrderClicks.onNext(order);
                }, Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.btnDelivered)
                .subscribe(o -> {
                    order.setButtonId(holder.mBinding.btnDelivered.getId());
                    mOrderClicks.onNext(order);
                }, Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.btnAcceptReceive)
                .subscribe(o -> {
                    order.setButtonId(holder.mBinding.btnAcceptReceive.getId());
                    mOrderClicks.onNext(order);
                }, Throwable::printStackTrace));

        holder.mDisposable.add(RxView.clicks(holder.mBinding.btnRejectReceive)
                .subscribe(o -> {
                    order.setButtonId(holder.mBinding.btnRejectReceive.getId());
                    mOrderClicks.onNext(order);
                }, Throwable::printStackTrace));

    }

    private void setupViews(ItemOrderBinding binding, Order order) {
        boolean isShipper = userId.equals(order.getShipper().getId());

        ViewUtil.toggleView(binding.tvShipper, !isShipper);
        ViewUtil.toggleView(binding.tvReceiver, isShipper);

        ViewUtil.setText(binding.tvProductName, order.getTitle());
        String total = TextUtil.formatCurrency((order.getProductPrice() + order.getShippingPrice()) * order.getItem().getAmount());
        ViewUtil.setText(binding.tvNetPrice, total + " VND");
        ViewUtil.setText(binding.amount, order.getItem().getAmount() + "");

        binding.tvStatus.setText(getStatus(isShipper, order.getStatus()));

        switch (order.getStatus().toLowerCase()) {
            case "paid":
                if (isShipper) {
                    ViewUtil.toggleView(binding.layoutDeliverStatus, true);
                    ViewUtil.toggleView(binding.layoutDeliveredStatus, false);
                    ViewUtil.toggleView(binding.layoutReceiveStatus, false);
                } else {
                    hideAllStatus(binding);
                }
                break;

            case "accepted":
                if (isShipper) {
                    ViewUtil.toggleView(binding.layoutDeliverStatus, false);
                    ViewUtil.toggleView(binding.layoutDeliveredStatus, true);
                    ViewUtil.toggleView(binding.layoutReceiveStatus, false);
                } else {
                    hideAllStatus(binding);
                }
                break;

            case "reject":
                hideAllStatus(binding);
                break;
            case "delivered":
                if (isShipper) {
                    hideAllStatus(binding);
                } else {
                    ViewUtil.toggleView(binding.layoutDeliverStatus, false);
                    ViewUtil.toggleView(binding.layoutDeliveredStatus, false);
                    ViewUtil.toggleView(binding.layoutReceiveStatus, true);
                }
                break;

            default:
                hideAllStatus(binding);
        }
    }

    private void hideAllStatus(ItemOrderBinding binding) {
        ViewUtil.toggleView(binding.layoutDeliverStatus, false);
        ViewUtil.toggleView(binding.layoutDeliveredStatus, false);
        ViewUtil.toggleView(binding.layoutReceiveStatus, false);
    }

    private String getStatus(boolean isShipper, String status) {
        status = status.toLowerCase();
        if (status.equals("pending")) {
            return "Chờ thanh toán";
        } else if (status.equals("paid")) {
            if (isShipper) {
                return "Đã thanh toán";
            }
            return "Chờ xác nhận";
        } else if (status.equals("accepted")) {
            if (isShipper) {
                return "Đã xác nhận";
            }
            return "Chờ giao hàng";
        } else if (status.equals("rejected")) {
            if (isShipper) {
                return "Đã từ chối";
            }
            return "Đã bị từ chối";
        } else if (status.equals("delivered")) {
            if (isShipper) {
                return "Chờ xác nhận";
            }
            return "Đã giao hàng";
        } else if (status.equals("success")) {
            return "Giao dịch thành công";
        } else if (status.equals("failed")) {
            return "Giao dịch thất bại";
        }

        return isShipper ? "Đã giao hàng" : "Đã nhận hàng";
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    static class CartHolder extends RecyclerView.ViewHolder {
        private final CompositeDisposable mDisposable = new CompositeDisposable();
        private ItemOrderBinding mBinding;

        public CartHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
