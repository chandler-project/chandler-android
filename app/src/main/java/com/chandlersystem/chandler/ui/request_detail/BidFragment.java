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
import com.chandlersystem.chandler.databinding.FragmentBidBinding;
import com.chandlersystem.chandler.ui.adapters.BidAdapter;

import java.util.ArrayList;
import java.util.List;

public class BidFragment extends Fragment {
    private FragmentBidBinding mBinding;
    private BidAdapter mBidAdapter;

    public BidFragment() {
        // Required empty public constructor
    }

    public static BidFragment newInstance() {
        return new BidFragment();
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
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        List<String> bidList = new ArrayList<>();
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        bidList.add("");
        mBidAdapter = new BidAdapter(getContext(), bidList);
        mBinding.recyclerViewActivities.setLayoutManager(layoutManager);
        mBinding.recyclerViewActivities.setNestedScrollingEnabled(true);
        mBinding.recyclerViewActivities.setHasFixedSize(true);
        mBinding.recyclerViewActivities.setAdapter(mBidAdapter);
    }
}
