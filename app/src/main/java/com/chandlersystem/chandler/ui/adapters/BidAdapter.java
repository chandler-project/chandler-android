package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.retrofit.Deal;
import com.chandlersystem.chandler.databinding.ItemBidBinding;
import com.chandlersystem.chandler.databinding.ItemCartTransactionBinding;
import com.chandlersystem.chandler.ui.deal_detail.DealDetailActivity;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.BidHolder> {
    private static final String TAG = BidAdapter.class.getCanonicalName();

    private Context mContext;

    private List<String> mBidList;

    private final PublishSubject<String> mBidClick = PublishSubject.create();


    public PublishSubject<String> getDealClick() {
        return mBidClick;
    }

    public BidAdapter(Context context, List<String> dealList) {
        this.mContext = context;
        this.mBidList = dealList;
    }

    @Override
    public BidHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BidHolder(LayoutInflater.from(mContext).inflate(R.layout.item_bid, parent, false));
    }

    @Override
    public void onBindViewHolder(BidHolder holder, int position) {
        String string = mBidList.get(position);

        setupViews(holder.mBinding, string);
        handleEvents(holder, position);
    }

    private void handleEvents(BidHolder holder, int position) {
        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = RxView.clicks(holder.mBinding.btnAccept)
                .subscribe(o -> {
                    Toast.makeText(mContext, "Hoora!!!!", Toast.LENGTH_SHORT).show();
                }, Throwable::printStackTrace);
    }

    private void setupViews(ItemBidBinding binding, String string) {
        ViewUtil.setImage(binding.layoutProfile.ivProfile, R.drawable.avatar);
        ViewUtil.setText(binding.layoutProfile.tvUserName, "Nguyen Hoang Phat");
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, "1000");
        ViewUtil.setText(binding.tvContent, "The cheapest price is here. Let me bring it to you");
        ViewUtil.setText(binding.tvBidPrice, "Bid gia: 20$");
    }

    @Override
    public int getItemCount() {
        return mBidList.size();
    }

    static class BidHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemBidBinding mBinding;

        public BidHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
