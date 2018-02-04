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
import com.chandlersystem.chandler.databinding.ItemCartTransactionBinding;
import com.chandlersystem.chandler.ui.deal_detail.DealDetailActivity;
import com.chandlersystem.chandler.utilities.TextUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private static final String TAG = CartAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Order> mOrderList;

    private String userId;

    private final PublishSubject<String> mOrderClicks = PublishSubject.create();

    private final PublishSubject<Order> mDisclaimClicks = PublishSubject.create();

    public PublishSubject<Order> getDisclaimClicks() {
        return mDisclaimClicks;
    }

    public PublishSubject<String> getOrderClicks() {
        return mOrderClicks;
    }

    public CartAdapter(Context mContext, List<Order> mOrderList, String userId) {
        this.mContext = mContext;
        this.mOrderList = mOrderList;
        this.userId = userId;
    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartHolder(LayoutInflater.from(mContext).inflate(R.layout.item_cart_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(CartHolder holder, int position) {
        Order order = mOrderList.get(position);

        setupViews(holder.mBinding, order);
        handleEvents(holder, position);
    }

    private void handleEvents(CartHolder holder, int position) {
        holder.mBinding.ivClose.setOnClickListener(view -> showAlertDialog(holder));

        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = RxView.clicks(holder.itemView)
                .subscribe(o -> {

                }, Throwable::printStackTrace);
    }

    private void startDealDetailActivity(Deal deal) {
        if (mContext == null) {
            return;
        }

        mContext.startActivity(DealDetailActivity.getInstance(mContext, deal));
    }

    private void setupViews(ItemCartTransactionBinding binding, Order order) {
        ViewUtil.setText(binding.tvProductName, order.getTitle());
        String total = TextUtil.formatCurrency((order.getProductPrice() + order.getShippingPrice()) * order.getItem().getAmount());
        ViewUtil.setText(binding.tvNetPrice, total + " VND");
        ViewUtil.setText(binding.amount, order.getItem().getAmount() + "");
        binding.tvStatus.setText(getStatus(userId.equals(order.getShipper().getId()), order.getStatus()));
    }

    private String getStatus(boolean isShipper, String status) {
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
        }

        return isShipper ? "Đã giao hàng" : "Đã nhận hàng";
    }

    private void showAlertDialog(CartHolder holder) {
        if (mContext == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String title = mContext.getString(R.string.content_remove_item);
        String content = mContext.getString(R.string.content_do_you_want_to_remove_this_deal);
        String buttonYes = mContext.getString(R.string.content_yes);
        String buttonNo = mContext.getString(R.string.content_no);
        builder.setTitle(title)
                .setCancelable(false)
                .setMessage(content)
                .setPositiveButton(buttonYes, (dialogInterface, i) -> {
                    int position = holder.getAdapterPosition();
                    mDisclaimClicks.onNext(mOrderList.get(position));
                    dialogInterface.dismiss();
                    removeAt(position);
                })
                .setNegativeButton(buttonNo, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    private void removeAt(int position) {
        mOrderList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    static class CartHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemCartTransactionBinding mBinding;

        public CartHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
