package com.chandlersystem.chandler.ui.requests;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.RxBus;
import com.chandlersystem.chandler.custom_views.LinearItemDecoration;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.events.BidRequestUpdate;
import com.chandlersystem.chandler.data.events.BuyDealUpdate;
import com.chandlersystem.chandler.data.events.CreateDealSuccess;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.data.models.request.BidRequestBody;
import com.chandlersystem.chandler.data.models.response.RetrofitResponseListItem;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.FragmentRequestListBinding;
import com.chandlersystem.chandler.ui.main.MainActivity;
import com.chandlersystem.chandler.ui.request_detail.RequestDetailActivity;
import com.chandlersystem.chandler.ui.requests.dummy.DummyContent;
import com.chandlersystem.chandler.ui.requests.dummy.DummyContent.DummyItem;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class RequestsFragment extends Fragment {
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;

    private RequestAdapter mAdapter;

    private FragmentRequestListBinding mBinding;

    @Inject
    ChandlerApi mApi;

    public RequestsFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.getActivityComponent().inject(this);
        }
    }

    public static RequestsFragment newInstance(int columnCount) {
        RequestsFragment fragment = new RequestsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        callApiGetRequest();
        handleEvents();
    }

    private void setupViews() {
        if (mColumnCount <= 1) {
            mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mBinding.list.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        mBinding.list.addItemDecoration(new LinearItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_normal)));
        mBinding.list.setEmptyView(mBinding.layoutEmpty.layoutEmpty);
        mBinding.layoutEmpty.tvEmpty.setText(getText(R.string.content_there_is_no_request));
        ViewUtil.setImage(mBinding.layoutEmpty.ivEmpty, R.drawable.ic_empty_rocket);
    }

    private void handleEvents() {
        mCompositeDisposable.add(RxSwipeRefreshLayout.refreshes(mBinding.layoutSwipe)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    mBinding.layoutSwipe.setRefreshing(false);
                    callApiGetRequest();
                }, Throwable::printStackTrace));

        mCompositeDisposable.add(RxBus.getInstance()
                .register(CreateDealSuccess.class, createDealSuccess -> callApiGetRequest(),
                        Throwable::printStackTrace));

        mCompositeDisposable.add(RxBus.getInstance()
                .register(BidRequestUpdate.class, createDealSuccess -> callApiGetRequest(), Throwable::printStackTrace));
    }

    private void callApiGetRequest() {
        mCompositeDisposable.add(mApi.getRequestNewFeed(UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(retrofitResponseListItem -> setAdapter(retrofitResponseListItem.items),
                        Throwable::printStackTrace));
    }

    private void setAdapter(List<Request> items) {
        mAdapter = new RequestAdapter(items, getContext());
        mBinding.list.setAdapter(mAdapter);

        requestClicks();
        requestBidClick();
    }

    private void requestBidClick() {
        mCompositeDisposable.add(mAdapter.getRequestBidClick()
                .subscribe(request -> {
                    DialogUtil.showBidDialog(getActivity(), request);
                }, Throwable::printStackTrace));
    }

    private void requestClicks() {
        mCompositeDisposable.add(mAdapter.getRequestClicks()
                .subscribe(this::startRequestDetailActivity, Throwable::printStackTrace));
    }

    private void startRequestDetailActivity(Request request) {
        Intent intent = RequestDetailActivity.getInstance(getContext(), request);
        startActivity(intent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCompositeDisposable.clear();
    }
}
