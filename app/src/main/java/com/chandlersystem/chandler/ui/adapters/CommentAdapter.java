package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.retrofit.Deal;
import com.chandlersystem.chandler.databinding.ItemUserActivityBinding;
import com.chandlersystem.chandler.databinding.ItemUserCommentBinding;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private static final String TAG = CommentAdapter.class.getCanonicalName();

    private Context mContext;

    private List<Deal> mDealList;

    private final PublishSubject<Deal> mDealClicks = PublishSubject.create();


    public PublishSubject<Deal> getDealClicks() {
        return mDealClicks;
    }

    public CommentAdapter(Context context, List<Deal> categories) {
        this.mContext = context;
        this.mDealList = categories;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(mContext).inflate(R.layout.item_user_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Deal deal = mDealList.get(position);

        setupViews(holder.mBinding, deal);
    }

    private void setupViews(ItemUserCommentBinding binding, Deal deal) {
        ViewUtil.showImage(mContext, "http://lorempixel.com/50/50/sports/1/", binding.layoutProfile.ivProfile);
        ViewUtil.setText(binding.layoutProfile.tvUserName, "Serious Bee");
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, "12k");
        ViewUtil.setText(binding.tvTime, "20/7/2018");
        ViewUtil.showImage(mContext, "http://lorempixel.com/400/300/sports/1/", binding.ivComment);
        ViewUtil.toggleView(binding.ivComment, true);
        ViewUtil.setText(binding.tvComment, "Lorem ipsum");
    }

    @Override
    public int getItemCount() {
        return mDealList.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder {
        private Disposable mDisposable;
        private ItemUserCommentBinding mBinding;

        public CommentHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
