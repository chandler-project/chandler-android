package com.chandlersystem.chandler.ui.deal_detail;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.data.models.pojo.Owner;
import com.chandlersystem.chandler.data.models.pojo.Shipper;
import com.chandlersystem.chandler.databinding.FragmentDealInforBinding;
import com.chandlersystem.chandler.ui.adapters.ImagePagerAdapter;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class DealInforFragment extends Fragment {
    private FragmentDealInforBinding mBinding;

    private List<String> mProductImageList;
    private ImagePagerAdapter mImagePagerAdapter;

    private static final String ARGUMENT_DEAL = "argument-deal";

    private Deal mDeal;

    public DealInforFragment() {
        // Required empty public constructor
    }

    public static DealInforFragment newInstance(Deal deal) {
        DealInforFragment fragment = new DealInforFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_deal_infor, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDeal = getArguments().getParcelable(ARGUMENT_DEAL);

        setupImageSlideShow();
        setDealTitle();
        setProfile();
        setPrice();
        setEndDealDate();
        setReactionAmount();
        setLinkPreview();
        setDealDetail();
    }

    private void setDealDetail() {
        ViewUtil.setText(mBinding.tvDealDetail, mDeal.getProductDesc());
    }

    private void setLinkPreview() {
        ViewUtil.setText(mBinding.layoutLinkPreview.tvLink, mDeal.getReference());
        ViewUtil.toggleView(mBinding.layoutLinkPreview.ivWebIcon, false);
        ViewUtil.toggleView(mBinding.layoutLinkPreview.ivWebPreview, false);
        ViewUtil.toggleView(mBinding.layoutLinkPreview.tvDescription, false);
    }

    private void setReactionAmount() {

    }

    private void setEndDealDate() {
        ViewUtil.setText(mBinding.tvEndDate, mDeal.getShippingTime());
    }

    private void setPrice() {
        ViewUtil.setText(mBinding.tvShippingPrice, getString(R.string.content_shipping_price) + mDeal.getShippingPrice() + mDeal.getCurrency());
        ViewUtil.setText(mBinding.tvPrice, mDeal.getPrice() + mDeal.getCurrency());
    }

    private void setProfile() {
        Shipper owner = mDeal.getShipper();
        if (ValidateUtil.checkString(owner.getAvatar())) {
            ViewUtil.showImage(getContext(), ApiConstant.BASE_URL_VER1 + owner.getAvatar(), mBinding.layoutProfile.ivProfile);
        } else {
            ViewUtil.setImage(mBinding.layoutProfile.ivProfile, R.drawable.ic_placeholder_avatar);
        }
        ViewUtil.setText(mBinding.layoutProfile.tvTitle, owner.getFullName());
        ViewUtil.setText(mBinding.tvOwner, owner.getFullName());
    }

    private void setDealTitle() {
        mBinding.tvDealTitle.setText(mDeal.getProductName());
    }

    private void setupImageSlideShow() {
        mProductImageList = mDeal.getProductPics();
        mImagePagerAdapter = new ImagePagerAdapter(getContext(), mProductImageList);
        mBinding.viewpagerSlideShow.setAdapter(mImagePagerAdapter);
        mBinding.viewpagerSlideShow.setOffscreenPageLimit(3);
        mBinding.indicator.setViewPager(mBinding.viewpagerSlideShow);
    }
}
