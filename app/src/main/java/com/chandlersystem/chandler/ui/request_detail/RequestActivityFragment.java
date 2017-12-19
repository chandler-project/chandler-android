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
import com.chandlersystem.chandler.data.models.retrofit.Deal;
import com.chandlersystem.chandler.databinding.FragmentDealActivityBinding;
import com.chandlersystem.chandler.databinding.FragmentRequestActivityBinding;
import com.chandlersystem.chandler.ui.adapters.UserActivityAdapter;

import java.util.ArrayList;
import java.util.List;

public class RequestActivityFragment extends Fragment {
    private FragmentRequestActivityBinding mBinding;
    private UserActivityAdapter mUserActivityAdapter;

    public RequestActivityFragment() {
        // Required empty public constructor
    }

    public static RequestActivityFragment newInstance() {
        return new RequestActivityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_activity, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        List<Deal> dealList = new ArrayList<>();
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        dealList.add(new Deal());
        mUserActivityAdapter = new UserActivityAdapter(getContext(), dealList);
        mBinding.recyclerViewActivities.setLayoutManager(layoutManager);
        mBinding.recyclerViewActivities.setNestedScrollingEnabled(true);
        mBinding.recyclerViewActivities.setHasFixedSize(true);
        mBinding.recyclerViewActivities.addItemDecoration(new LinearItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_small)));
        mBinding.recyclerViewActivities.setAdapter(mUserActivityAdapter);
    }
}
