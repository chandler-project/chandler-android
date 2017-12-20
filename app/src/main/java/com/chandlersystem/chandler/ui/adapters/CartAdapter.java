package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.retrofit.Deal;
import com.chandlersystem.chandler.databinding.ItemCartTransactionBinding;
import com.chandlersystem.chandler.databinding.ItemNotificationBinding;
import com.chandlersystem.chandler.ui.deal_detail.DealDetailActivity;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private static final String TAG = CartAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Deal> mDealList;

    private final PublishSubject<String> mDealClick = PublishSubject.create();


    public PublishSubject<String> getDealClick() {
        return mDealClick;
    }

    public CartAdapter(Context context, List<Deal> dealList) {
        this.mContext = context;
        this.mDealList = dealList;
    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartHolder(LayoutInflater.from(mContext).inflate(R.layout.item_cart_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(CartHolder holder, int position) {
        Deal deal = mDealList.get(position);

        setupViews(holder.mBinding, deal);
        handleEvents(holder, position);
    }

    private void handleEvents(CartHolder holder, int position) {
        holder.mBinding.ivClose.setOnClickListener(view -> showAlertDialog(holder, position));

        holder.mBinding.plus.setOnClickListener(view -> {
            int currentAmount = Integer.valueOf(holder.mBinding.amount.getText().toString());
            holder.mBinding.amount.setText(++currentAmount + "");
        });

        holder.mBinding.minus.setOnClickListener(view -> {
            int currentAmount = Integer.valueOf(holder.mBinding.amount.getText().toString());
            if (currentAmount > 0) {
                holder.mBinding.amount.setText(--currentAmount + "");
            }
        });

        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = RxView.clicks(holder.itemView)
                .subscribe(o -> {
                    startDealDetailActivity(mDealList.get(position));
                }, Throwable::printStackTrace);
    }

    private void startDealDetailActivity(Deal deal) {
        if (mContext == null) {
            return;
        }

        mContext.startActivity(DealDetailActivity.getInstance(mContext));
    }

    private void setupViews(ItemCartTransactionBinding binding, Deal deal) {
        ViewUtil.setText(binding.tvProductName, "This is the product name");
        ViewUtil.setText(binding.tvNetPrice, "Net price is $20");
        ViewUtil.setText(binding.tvShippingPrice, "Shipping price is $5- $10");
        ViewUtil.setText(binding.amount, "1");
    }

    private void showAlertDialog(CartHolder holder, int position) {
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
                    dialogInterface.dismiss();
                    removeAt(holder.getAdapterPosition());
                })
                .setNegativeButton(buttonNo, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    private void removeAt(int position) {
        mDealList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mDealList.size();
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
