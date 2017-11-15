package com.chandlersystem.chandler.ui.product_search;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.FragmentProductBinding;
import com.chandlersystem.chandler.ui.product_search.ProductSearchFragment.ProductSearchListener;
import com.chandlersystem.chandler.ui.product_search.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link ProductSearchListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final ProductSearchListener mListener;

    public ProductSearchAdapter(List<DummyItem> items, ProductSearchFragment.ProductSearchListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mBinding.productPrice.setText(mValues.get(position).id);
        holder.mBinding.productName.setText(mValues.get(position).content);

        holder.mBinding.getRoot().setOnClickListener(v -> {
            if (null != mListener) {
                //todo notify item selected
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public FragmentProductBinding mBinding;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mBinding.productName.getText() + "'";
        }
    }
}
