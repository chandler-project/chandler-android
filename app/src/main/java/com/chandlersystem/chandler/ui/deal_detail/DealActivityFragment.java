package com.chandlersystem.chandler.ui.deal_detail;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.RxBus;
import com.chandlersystem.chandler.custom_views.LinearItemDecoration;
import com.chandlersystem.chandler.data.events.BuyDealUpdate;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.data.models.pojo.Owner;
import com.chandlersystem.chandler.databinding.FragmentDealActivityBinding;
import com.chandlersystem.chandler.ui.adapters.UserActivityAdapter;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class DealActivityFragment extends Fragment {
    private FragmentDealActivityBinding mBinding;
    private UserActivityAdapter mUserActivityAdapter;

    private List<Owner> requeters;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Deal mDeal;

    private static final String ARGUMENT_DEAL = "argument-deal";

    public DealActivityFragment() {
        // Required empty public constructor
    }

    public static DealActivityFragment newInstance(Deal deal) {
        DealActivityFragment fragment = new DealActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_DEAL, deal);
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_deal_activity, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDeal = getArguments().getParcelable(ARGUMENT_DEAL);
        setupRecyclerView();
        handleEvent();
    }

    private void handleEvent() {
        mCompositeDisposable.add(RxBus.getInstance()
                .register(BuyDealUpdate.class, buyDealUpdate -> {
                    requeters.clear();
                    requeters.addAll(buyDealUpdate.requesters);
                    mUserActivityAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace));
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        requeters = new ArrayList<>();
        requeters.addAll(mDeal.getRequesters());
        mUserActivityAdapter = new UserActivityAdapter(getContext(), requeters);
        mBinding.recyclerViewActivities.setLayoutManager(layoutManager);
        mBinding.recyclerViewActivities.setNestedScrollingEnabled(true);
        mBinding.recyclerViewActivities.setHasFixedSize(true);
        mBinding.recyclerViewActivities.addItemDecoration(new LinearItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_small)));
        mBinding.recyclerViewActivities.setEmptyView(mBinding.layoutEmpty.layoutEmpty);
        mBinding.layoutEmpty.tvEmpty.setText(getText(R.string.content_there_is_no_requester));
        ViewUtil.setImage(mBinding.layoutEmpty.ivEmpty, R.drawable.ic_empty_user);
        mBinding.recyclerViewActivities.setAdapter(mUserActivityAdapter);
    }
}
