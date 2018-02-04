package com.chandlersystem.chandler.ui.request_detail;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.RxBus;
import com.chandlersystem.chandler.custom_views.LinearItemDecoration;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.events.BidRequestUpdate;
import com.chandlersystem.chandler.data.models.pojo.Bidder;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.FragmentBidBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.BidAdapter;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class BidFragment extends Fragment {
    private FragmentBidBinding mBinding;
    private BidAdapter mBidAdapter;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private static final String ARGUMENT_REQUEST = "argument-request";

    private Request mRequest;

    private List<Bidder> mBidders;

    @Inject
    ChandlerApi mApi;

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
    public void onAttach(Context context) {
        super.onAttach(context);

        ActivityComponent mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(getActivity()))
                .applicationComponent(ChandlerApplication
                        .getApplicationComponent(getContext()))
                .build();

        mActivityComponent.inject(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCompositeDisposable.clear();
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
        setAdapter();
        handleEvents();
    }

    private void handleEvents() {
        mCompositeDisposable.add(RxBus.getInstance()
                .register(BidRequestUpdate.class, bidRequestUpdate -> {
                    mBidders.clear();
                    mBidders.addAll(bidRequestUpdate.bidders);
                    mBidAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace));
    }

    private void setAdapter() {
        mBidders = new ArrayList<>();
        mBidders.addAll(mRequest.getBidders());
        String userId = UserManager.getUserSync().getId();
        String requestOwnerId = mRequest.getOwner().getId();
        mBidAdapter = new BidAdapter(getContext(), mBidders, userId.equals(requestOwnerId), mRequest.getStatus().equals("Ordered"));
        mBinding.recyclerViewBids.setAdapter(mBidAdapter);
        chooseBidder();
    }

    private void chooseBidder() {
        if (mBidAdapter == null) {
            return;
        }

        mCompositeDisposable.add(mBidAdapter.getBidClick()
                .subscribe(this::chooseBidderApi, Throwable::printStackTrace));
    }

    private void chooseBidderApi(Bidder bidder) {
        mCompositeDisposable.add(mApi.chooseShipperForRequest(mRequest.getId(), bidder.getId(), UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(retrofitResponseItem -> {
                    chooseShipperSuccess();
                }, Throwable::printStackTrace));
    }

    private void chooseShipperSuccess() {
        Toast.makeText(getContext(), R.string.content_ordered, Toast.LENGTH_SHORT).show();
        mRequest.setStatus("Ordered");
        setAdapter();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewBids.setLayoutManager(layoutManager);
        mBinding.recyclerViewBids.setNestedScrollingEnabled(true);
        mBinding.recyclerViewBids.setHasFixedSize(true);
        mBinding.recyclerViewBids.setEmptyView(mBinding.layoutEmpty.layoutEmpty);
        mBinding.layoutEmpty.tvEmpty.setText(getText(R.string.content_there_is_no_bidder));
        ViewUtil.setImage(mBinding.layoutEmpty.ivEmpty, R.drawable.ic_empty_user);
    }
}
