package com.chandlersystem.chandler.ui.request_detail;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.custom_views.LinearItemDecoration;
import com.chandlersystem.chandler.data.models.pojo.Bidder;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.databinding.FragmentBidBinding;
import com.chandlersystem.chandler.ui.adapters.BidAdapter;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class BidFragment extends Fragment {
    private FragmentBidBinding mBinding;
    private BidAdapter mBidAdapter;

    private static final String ARGUMENT_REQUEST = "argument-request";

    private Request mRequest;

    public BidFragment() {
        // Required empty public constructor
    }

    public static BidFragment newInstance(Request request) {
        BidFragment fragment = new BidFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_REQUEST, request);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bid, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRequest = getArguments().getParcelable(ARGUMENT_REQUEST);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        List<Bidder> bidList = mRequest.getBidders();
        mBidAdapter = new BidAdapter(getContext(), bidList);
        mBinding.recyclerViewBids.setLayoutManager(layoutManager);
        mBinding.recyclerViewBids.setNestedScrollingEnabled(true);
        mBinding.recyclerViewBids.setHasFixedSize(true);
        mBinding.recyclerViewBids.setEmptyView(mBinding.layoutEmpty.layoutEmpty);
        mBinding.layoutEmpty.tvEmpty.setText(getText(R.string.content_there_is_no_bidder));
        ViewUtil.setImage(mBinding.layoutEmpty.ivEmpty, R.drawable.ic_empty_user);
        mBinding.recyclerViewBids.setAdapter(mBidAdapter);
    }
}
