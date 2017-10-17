package com.chandlersystem.chandler.ui.splash.onboarding;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.FragmentSecondOnboardingBinding;
import com.chandlersystem.chandler.utilities.ViewUtil;

public class SecondOnboardingFragment extends Fragment {
    private FragmentSecondOnboardingBinding mBinding;

    public SecondOnboardingFragment() {
        // Required empty public constructor
    }

    public static SecondOnboardingFragment newInstance() {
        SecondOnboardingFragment fragment = new SecondOnboardingFragment();
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
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_second_onboarding, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtil.showImage(getContext(), "http://media3.giphy.com/media/kEKcOWl8RMLde/giphy.gif", mBinding.layoutOnboarding.ivOnboardingImage, R.drawable.ic_placeholder);
        mBinding.layoutOnboarding.tvTitle.setText(getString(R.string.normal_text));
    }
}
