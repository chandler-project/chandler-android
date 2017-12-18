package com.chandlersystem.chandler.ui.notification;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.custom_views.LinearItemDecoration;
import com.chandlersystem.chandler.databinding.FragmentNotificationBinding;
import com.chandlersystem.chandler.ui.adapters.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding mBinding;

    private NotificationAdapter mAdapter;


    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        setupAdapter();
    }

    private void setupAdapter() {
        List<String> mNotificationList = new ArrayList<>();
        mNotificationList.add(new String("Hello world"));
        mNotificationList.add(new String("Hello world"));
        mNotificationList.add(new String("Hello world"));
        mNotificationList.add(new String("Hello world"));
        mNotificationList.add(new String("Hello world"));
        mNotificationList.add(new String("Hello world"));
        mNotificationList.add(new String("Hello world"));
        mNotificationList.add(new String("Hello world"));
        mNotificationList.add(new String("Hello world"));
        mNotificationList.add(new String("Hello world"));
        mNotificationList.add(new String("Hello world"));
        mNotificationList.add(new String("Hello world"));
        mAdapter = new NotificationAdapter(getContext(), mNotificationList);
        mBinding.rvNotifications.setAdapter(mAdapter);
    }

    private void setupViews() {
        setupNotificationRecyclerView();
    }

    private void setupNotificationRecyclerView() {
        mBinding.rvNotifications.setHasFixedSize(true);
        mBinding.rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
