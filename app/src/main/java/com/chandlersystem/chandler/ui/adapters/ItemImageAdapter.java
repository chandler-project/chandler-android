package com.chandlersystem.chandler.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.request.RequestOptions;
import com.chandlersystem.chandler.GlideApp;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ItemImageBinding;
import com.chandlersystem.chandler.databinding.ItemRemoveImageBinding;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class ItemImageAdapter extends
        RecyclerView.Adapter {
    private static final String TAG = ItemImageAdapter.class.getSimpleName();
    private static final int TYPE_ADD_IMAGE = 0;
    private static final int TYPE_REMOVE_IMAGE = 1;

    private Context mContext;
    private ArrayList<String> mDataset;

    private final PublishSubject<Integer> mAddImageClicks = PublishSubject.create();

    public PublishSubject<Integer> getAddImageClicks() {
        return mAddImageClicks;
    }

    public ItemImageAdapter(Context context) {
        this.mContext = context;
        this.mDataset = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ADD_IMAGE:
                View addImageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
                return new AddImageHolder(addImageView);
            default:
                View removeImageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_remove_image, parent, false);
                return new RemoveImageHolder(removeImageView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddImageHolder) {
            handleEventAddImage((AddImageHolder) holder, position);
        } else {
            String imagePath = mDataset.get(position);
            setupRemoveImageView((RemoveImageHolder) holder, imagePath);
            handleEventRemoveImage((RemoveImageHolder) holder, position);
        }
    }

    private void handleEventAddImage(AddImageHolder holder, int position) {
        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = (RxView.clicks(holder.mBinding.layoutAddImage)
                .subscribe(o -> mAddImageClicks.onNext(position), Throwable::printStackTrace));
    }

    private void handleEventRemoveImage(RemoveImageHolder holder, int position) {
        if (holder.mDisposable != null && !holder.mDisposable.isDisposed()) {
            holder.mDisposable.dispose();
        }

        holder.mDisposable = (RxView.clicks(holder.mBinding.ivRemove)
                .subscribe(o -> removeImage(position), Throwable::printStackTrace));
    }

    private void setupRemoveImageView(RemoveImageHolder holder, String imagePath) {
        boolean isImagePathValidated = ValidateUtil.checkString(imagePath);

        if (isImagePathValidated) {
            Uri imagePathUri = Uri.parse(imagePath);
            GlideApp.with(mContext)
                    .load(imagePathUri)
                    .into(holder.mBinding.ivPhoto);
            ViewUtil.setText(holder.mBinding.tvImageName, imagePath);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDataset.size()) {
            return TYPE_ADD_IMAGE;
        }

        return TYPE_REMOVE_IMAGE;
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }

    public void addImage(String fileUrl) {
        mDataset.add(fileUrl);
        notifyDataSetChanged();
    }

    public void addImage(List<String> list) {
        mDataset.addAll(list);
        notifyDataSetChanged();
    }

    public void removeImage(int position) {
        mDataset.remove(position);
        notifyDataSetChanged();
    }

    public ArrayList<String> getData() {
        return mDataset;
    }

    static class AddImageHolder extends RecyclerView.ViewHolder {
        private ItemImageBinding mBinding;
        private Disposable mDisposable;

        public AddImageHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    static class RemoveImageHolder extends RecyclerView.ViewHolder {
        private ItemRemoveImageBinding mBinding;
        private Disposable mDisposable;

        public RemoveImageHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
