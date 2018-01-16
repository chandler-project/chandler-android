package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.models.pojo.Feedback;
import com.chandlersystem.chandler.data.models.pojo.Owner;
import com.chandlersystem.chandler.databinding.FragmentReviewBinding;
import com.chandlersystem.chandler.ui.profile.UserProfileActivity;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    private final List<Feedback> mValues;

    private Context mContext;

    public FeedbackAdapter(List<Feedback> mValues, Context mContext) {
        this.mValues = mValues;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Feedback feedback = mValues.get(position);

        Owner owner = feedback.getOwner();
        if (ValidateUtil.checkString(owner.getAvatar())) {
            ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + owner.getAvatar(), holder.mBinding.layoutProfile.ivProfile);
        } else {
            ViewUtil.setImage(holder.mBinding.layoutProfile.ivProfile, R.drawable.ic_placeholder_avatar);
        }

        ViewUtil.setText(holder.mBinding.layoutProfile.tvUserName, owner.getFullName());
        ViewUtil.setText(holder.mBinding.layoutProfile.tvUserPoint, owner.getPoints() + owner.getPoints() > 2 ? mContext.getString(R.string.content_points) : mContext.getString(R.string.content_point));

        holder.mBinding.content.setText(feedback.getContent());

        holder.mBinding.layoutProfile.layoutProfile.setOnClickListener(v -> {
            mContext.startActivity(UserProfileActivity.getIntent(mContext));
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FragmentReviewBinding mBinding;

        public ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
        }

    }
}
