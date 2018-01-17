package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.models.pojo.Comment;
import com.chandlersystem.chandler.data.models.pojo.Owner;
import com.chandlersystem.chandler.data.models.pojo.Shipper;
import com.chandlersystem.chandler.databinding.ItemUserCommentBinding;
import com.chandlersystem.chandler.ui.profile.UserProfileActivity;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private static final String TAG = CommentAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Comment> mCommentList;

    private final PublishSubject<Comment> mCommentClicks = PublishSubject.create();


    public PublishSubject<Comment> getCommentClicks() {
        return mCommentClicks;
    }

    public CommentAdapter(Context context, List<Comment> categories) {
        this.mContext = context;
        this.mCommentList = categories;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(mContext).inflate(R.layout.item_user_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Comment comment = mCommentList.get(position);
        setupViews(holder.mBinding, comment);
        handleEvents(holder, comment);
    }

    private void handleEvents(CommentHolder holder, Comment comment) {
        if (!holder.mDisposable.isDisposed()) {
            holder.mDisposable.clear();
        }

        holder.mDisposable.add(RxView.clicks(holder.mBinding.layoutProfile.layoutProfile)
                .subscribe(o -> {
                    mContext.startActivity(UserProfileActivity.getIntent(mContext, comment.getOwner().getId()));
                }, Throwable::printStackTrace));
    }

    private void setupViews(ItemUserCommentBinding binding, Comment comment) {
        if (comment.getImages() != null && !comment.getImages().isEmpty() && ValidateUtil.checkString(comment.getImages().get(0))) {
            ViewUtil.showImage(mContext, comment.getImages().get(0), binding.ivComment);
            ViewUtil.toggleView(binding.ivComment, true);
        } else {
            ViewUtil.toggleView(binding.ivComment, false);
        }

        Owner owner = comment.getOwner();
        if (ValidateUtil.checkString(owner.getAvatar())) {
            ViewUtil.showImage(mContext, ApiConstant.BASE_URL_VER1 + owner.getAvatar(), binding.layoutProfile.ivProfile);
        } else {
            ViewUtil.setImage(binding.layoutProfile.ivProfile, R.drawable.ic_placeholder_avatar);
        }

        ViewUtil.setText(binding.layoutProfile.tvUserName, owner.getFullName());
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, owner.getPoints() + " " + (owner.getPoints() > 2 ? mContext.getString(R.string.content_points) : mContext.getString(R.string.content_point)));
        ViewUtil.setText(binding.tvComment, comment.getContent());
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder {
        private final CompositeDisposable mDisposable = new CompositeDisposable();
        private ItemUserCommentBinding mBinding;

        public CommentHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
