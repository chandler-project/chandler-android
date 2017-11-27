package com.chandlersystem.chandler.ui.createrequest;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chandlersystem.chandler.GlideApp;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.ItemImageBinding;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class ItemImageAdapter extends
        RecyclerView.Adapter<ItemImageAdapter.ImageHolder> {
    private static final String TAG = ItemImageAdapter.class.getSimpleName();
    private final int TYPE_IMAGE = 1;
    private final int TYPE_ADD_IMAGE = 2;

    private ArrayList<String> mDataset;
    private CreateRequestActivity.OnAddImageClick mListener;

    public ItemImageAdapter(CreateRequestActivity.OnAddImageClick onAddImageClick) {
        mDataset = new ArrayList<>();
        mListener = onAddImageClick;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ADD_IMAGE:
                holder.mBinding.itemImageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
            case TYPE_IMAGE:
                holder.mBinding.itemImageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Uri imagePath = Uri.parse(mDataset.get(position));
                GlideApp.with(holder.mBinding.itemImageButton.getContext())
                        .load(imagePath)
                        .apply(new RequestOptions().transform(new RoundedCornersTransformation(30, 0, RoundedCornersTransformation.CornerType.ALL)))
                        .into(holder.mBinding.itemImageButton);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == mDataset.size() ? TYPE_ADD_IMAGE : TYPE_IMAGE;
    }

    public void addImage(String fileUrl) {
        mDataset.add(fileUrl);
        notifyItemInserted(mDataset.size() - 1);
    }

    public void addImage(List<String> list){
        mDataset.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<String> getData() {
        return mDataset;
    }

    class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemImageBinding mBinding;

        public ImageHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mBinding.itemImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.item_imageButton) {
                if (getAdapterPosition() == mDataset.size()) {
                    mListener.onAddImageClick();
                }
            }
        }
    }
}
