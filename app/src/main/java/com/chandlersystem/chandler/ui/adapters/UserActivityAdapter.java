package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.models.pojo.Owner;
import com.chandlersystem.chandler.data.models.pojo.Owner;
import com.chandlersystem.chandler.data.models.pojo.Shipper;
import com.chandlersystem.chandler.databinding.ItemUserActivityBinding;
import com.chandlersystem.chandler.ui.profile.UserProfileActivity;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class UserActivityAdapter extends RecyclerView.Adapter<UserActivityAdapter.UserActivityHolder> {
    private static final String TAG = UserActivityAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Owner> mOwnerList;

    private final PublishSubject<Owner> mOwnerClicks = PublishSubject.create();


    public PublishSubject<Owner> getOwnerClicks() {
        return mOwnerClicks;
    }

    public UserActivityAdapter(Context context, List<Owner> categories) {
        this.mContext = context;
        this.mOwnerList = categories;
    }

    @Override
    public UserActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserActivityHolder(LayoutInflater.from(mContext).inflate(R.layout.item_user_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(UserActivityHolder holder, int position) {
        Owner owner = mOwnerList.get(position);
        setupViews(holder.mBinding, owner);
        handleEvents(holder, owner);
    }

    private void handleEvents(UserActivityHolder holder, Owner owner) {
        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = (RxView.clicks(holder.mBinding.layoutProfile.layoutProfile)
                .subscribe(o -> {
                    mContext.startActivity(UserProfileActivity.getIntent(mContext, owner.getId()));
                }, Throwable::printStackTrace));
    }

    private void setupViews(ItemUserActivityBinding binding, Owner owner) {
        if (ValidateUtil.checkString(owner.getAvatar())) {
            ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + owner.getAvatar(), binding.layoutProfile.ivProfile);
        } else {
            ViewUtil.setImage(binding.layoutProfile.ivProfile, R.drawable.ic_placeholder_avatar);
        }
        ViewUtil.setText(binding.layoutProfile.tvUserName, owner.getFullName());
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, owner.getPoints() + (owner.getPoints() > 2 ? mContext.getString(R.string.content_points) : mContext.getString(R.string.content_point)));
    }

    @Override
    public int getItemCount() {
        return mOwnerList.size();
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
