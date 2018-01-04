package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.databinding.ItemUserActivityBinding;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class UserActivityAdapter extends RecyclerView.Adapter<UserActivityAdapter.UserActivityHolder> {
    private static final String TAG = UserActivityAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Deal> mDealList;

    private final PublishSubject<Deal> mDealClicks = PublishSubject.create();


    public PublishSubject<Deal> getDealClicks() {
        return mDealClicks;
    }

    public UserActivityAdapter(Context context, List<Deal> categories) {
        this.mContext = context;
        this.mDealList = categories;
    }

    @Override
    public UserActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserActivityHolder(LayoutInflater.from(mContext).inflate(R.layout.item_user_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(UserActivityHolder holder, int position) {
        Deal deal = mDealList.get(position);

        setupViews(holder.mBinding, deal);
    }

    private void setupViews(ItemUserActivityBinding binding, Deal deal) {
        ViewUtil.showImage(mContext, "http://lorempixel.com/50/50/sports/1/", binding.layoutProfile.ivProfile);
        ViewUtil.setText(binding.layoutProfile.tvUserName, "Serious Bee");
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, "12k");
        ViewUtil.setText(binding.tvTime, "20/7/2018");
    }

    @Override
    public int getItemCount() {
        return mDealList.size();
    }

    static class UserActivityHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemUserActivityBinding mBinding;

        public UserActivityHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
