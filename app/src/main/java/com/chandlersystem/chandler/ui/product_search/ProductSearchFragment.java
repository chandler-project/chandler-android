package com.chandlersystem.chandler.ui.product_search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.FragmentProductListBinding;
import com.chandlersystem.chandler.ui.product_search.dummy.DummyContent;
import com.chandlersystem.chandler.ui.product_search.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link ProductSearchListener}
 * interface.
 */
public class ProductSearchFragment extends Fragment {

    private int mColumnCount = 1;
    private ProductSearchListener mListener;
    private FragmentProductListBinding mBinding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductSearchFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductSearchFragment newInstance() {
        ProductSearchFragment fragment = new ProductSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false);

        // Set the adapter
        if (mColumnCount <= 1) {
            mBinding.listRecommendedProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mBinding.listRecommendedProduct.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        mBinding.listRecommendedProduct.setAdapter(new ProductSearchAdapter(DummyContent.ITEMS, mListener));

        setUpListener();
        return mBinding.getRoot();
    }

    private void setUpListener() {
        mBinding.tvTopPick.setOnClickListener(view -> mListener.onTopPick());
        mBinding.tvTrending.setOnClickListener(view -> mListener.onTrending());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProductSearchListener) {
            mListener = (ProductSearchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProductSearchListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ProductSearchListener {

        void onTopPick();

        void onTrending();
    }
}
