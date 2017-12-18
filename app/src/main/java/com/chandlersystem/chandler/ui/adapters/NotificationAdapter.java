package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.retrofit.Deal;
import com.chandlersystem.chandler.databinding.ItemNotificationBinding;
import com.chandlersystem.chandler.databinding.ItemUserActivityBinding;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    private static final String TAG = NotificationAdapter.class.getCanonicalName();

    private Context mContext;

    private List<String> mNotificationList;

    private final PublishSubject<String> mNotificationClicks = PublishSubject.create();


    public PublishSubject<String> getNotificationClicks() {
        return mNotificationClicks;
    }

    public NotificationAdapter(Context context, List<String> notificationList) {
        this.mContext = context;
        this.mNotificationList = notificationList;
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationHolder(LayoutInflater.from(mContext).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        String notification = mNotificationList.get(position);

        setupViews(holder.mBinding, notification);
    }

    private void setupViews(ItemNotificationBinding binding, String notification) {
        ViewUtil.showImage(mContext, "http://lorempixel.com/50/50/sports/1/", binding.ivNofitication);
        ViewUtil.setText(binding.tvNotificationContent, "What is the lorem ipsum. What is the lorem ipsum");
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    static class NotificationHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemNotificationBinding mBinding;

        public NotificationHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
