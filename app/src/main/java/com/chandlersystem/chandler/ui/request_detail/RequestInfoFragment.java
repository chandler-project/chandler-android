package com.chandlersystem.chandler.ui.request_detail;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.models.pojo.Owner;
import com.chandlersystem.chandler.data.models.pojo.Request;
import com.chandlersystem.chandler.databinding.FragmentRequestInforBinding;
import com.chandlersystem.chandler.ui.adapters.ImagePagerAdapter;
import com.chandlersystem.chandler.ui.profile.UserProfileActivity;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.List;

public class RequestInfoFragment extends Fragment {
    private FragmentRequestInforBinding mBinding;

    private List<String> mProductImageList;
    private ImagePagerAdapter mImagePagerAdapter;

    private static final String ARGUMENT_REQUEST = "argument-request";

    private Request mRequest;

    public RequestInfoFragment() {
        // Required empty public constructor
    }

    public static RequestInfoFragment newInstance(Request request) {
        RequestInfoFragment fragment = new RequestInfoFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_infor, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRequest = getArguments().getParcelable(ARGUMENT_REQUEST);
        setupViews();
        handleEvents();
    }

    private void setupViews() {
        setupImageSlideShow();
        setDealTitle();
        setProfile();
        setPrice();
        setEndDealDate();
        setReactionAmount();
        setLinkPreview();
        setDealDetail();
    }

    private void handleEvents() {
        mBinding.layoutProfile.layoutProfile.setOnClickListener(v -> startActivity(UserProfileActivity.getIntent(getContext(), mRequest.getOwner().getId())));
    }

    private void setDealDetail() {
        ViewUtil.setText(mBinding.tvDealDetail, mRequest.getDescription());
    }

    private void setLinkPreview() {
        ViewUtil.setText(mBinding.layoutLinkPreview.tvLink, mRequest.getReference());
        ViewUtil.toggleView(mBinding.layoutLinkPreview.ivWebIcon, false);
        ViewUtil.toggleView(mBinding.layoutLinkPreview.ivWebPreview, false);
        ViewUtil.toggleView(mBinding.layoutLinkPreview.tvDescription, false);
    }

    private void setReactionAmount() {

    }

    private void setEndDealDate() {
        ViewUtil.setText(mBinding.tvEndDate, mRequest.getDeadline());
    }

    private void setPrice() {
        ViewUtil.setText(mBinding.tvShippingPrice, getString(R.string.content_request) + mRequest.getBudget().getMin() + " - " + mRequest.getBudget().getMax() + mRequest.getCurrency());
        ViewUtil.setText(mBinding.tvPrice, mRequest.getPrice() + mRequest.getCurrency());
    }

    private void setProfile() {
        Owner owner = mRequest.getOwner();
        if (ValidateUtil.checkString(owner.getAvatar())) {
            ViewUtil.showImage(getContext(), ApiConstant.BASE_URL_VER1 + owner.getAvatar(), mBinding.layoutProfile.ivProfile);
        } else {
            ViewUtil.setImage(mBinding.layoutProfile.ivProfile, R.drawable.ic_placeholder_avatar);
        }
        ViewUtil.setText(mBinding.layoutProfile.tvTitle, owner.getFullName());
        ViewUtil.setText(mBinding.tvOwner, owner.getFullName());
    }

    private void setDealTitle() {
        mBinding.tvDealTitle.setText(mRequest.getProductName());
    }

    private void setupImageSlideShow() {
        mProductImageList = mRequest.getProductPics();
        mImagePagerAdapter = new ImagePagerAdapter(getContext(), mProductImageList);
        mBinding.viewpagerSlideShow.setAdapter(mImagePagerAdapter);
        mBinding.viewpagerSlideShow.setOffscreenPageLimit(3);
        mBinding.indicator.setViewPager(mBinding.viewpagerSlideShow);
    }
}
