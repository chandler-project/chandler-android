package com.chandlersystem.chandler.ui.requests;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.data.models.retrofit.Deal;
import com.chandlersystem.chandler.databinding.FragmentRequestItemBinding;
import com.chandlersystem.chandler.databinding.ItemDealBinding;
import com.chandlersystem.chandler.ui.requests.RequestsFragment.OnListRequestFragmentInteractionListener;
import com.chandlersystem.chandler.ui.requests.dummy.DummyContent.DummyItem;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListRequestFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListRequestFragmentInteractionListener mListener;
    private Context mContext;

    public RequestAdapter(List<DummyItem> mValues, OnListRequestFragmentInteractionListener mListener, Context mContext) {
        this.mValues = mValues;
        this.mListener = mListener;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.itemView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onRequestFragmentInteraction(holder.mItem);
            }
        });
        setupViews(holder.mBinding);
    }

    private void setupViews(FragmentRequestItemBinding binding) {
        ViewUtil.showImage(mContext, "http://lorempixel.com/50/50/sports/2/", binding.layoutProfile.ivProfile);
        ViewUtil.setText(binding.layoutProfile.tvUserName, "Serious Bee");
        ViewUtil.setText(binding.tvDate, "Need it in 10/07/1018");
        ViewUtil.setText(binding.layoutProfile.tvUserPoint, "12k");
        ViewUtil.setText(binding.tvEndDate, "20/7/2018");
        ViewUtil.showImage(mContext, "http://lorempixel.com/400/320/sports/", binding.ivProduct);
        ViewUtil.setText(binding.tvPrice, "$200");
        ViewUtil.setText(binding.tvProductTitle, "What is Lorem Ipsum?");
        ViewUtil.setText(binding.tvProductLink, "https://google.com.vn");
        ViewUtil.setText(binding.tvProductDetail, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s");
        ViewUtil.setText(binding.tvAddress, "Da Nang");
        ViewUtil.setText(binding.layoutCategoryName.tvCategoryName, "Trending");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FragmentRequestItemBinding mBinding;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
        }

    }
}
