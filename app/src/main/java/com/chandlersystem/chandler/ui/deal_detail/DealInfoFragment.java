package com.chandlersystem.chandler.ui.deal_detail;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.configs.ApiConstant;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.data.models.pojo.Shipper;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.FragmentDealInforBinding;
import com.chandlersystem.chandler.databinding.ItemVoteBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.ImagePagerAdapter;
import com.chandlersystem.chandler.ui.profile.UserProfileActivity;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class DealInfoFragment extends Fragment {
    private FragmentDealInforBinding mBinding;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private List<String> mProductImageList;
    private ImagePagerAdapter mImagePagerAdapter;

    private static final String ARGUMENT_DEAL = "argument-deal";

    private Deal mDeal;

    public DealInfoFragment() {
        // Required empty public constructor
    }

    public static DealInfoFragment newInstance(Deal deal) {
        DealInfoFragment fragment = new DealInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_DEAL, deal);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    ChandlerApi mApi;

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
        mBinding.layoutProfile.layoutProfile.setOnClickListener(v -> startActivity(UserProfileActivity.getIntent(getContext())));

        mCompositeDisposable.add(RxView.clicks(mBinding.layoutVote.layoutUpvote)
                .subscribe(o -> {
                    clickUpvote();
                    upVote(mBinding.layoutVote, mDeal);
                }, Throwable::printStackTrace));

        mCompositeDisposable.add(RxView.clicks(mBinding.layoutVote.layoutDownvote)
                .subscribe(o -> {
                    clickDownVote();
                    downVote(mBinding.layoutVote, mDeal);
                }, Throwable::printStackTrace));
    }

    private void downVote(ItemVoteBinding holder, Deal deal) {
        int currentDownvote = deal.getDownvote();
        if (deal.isDownvoted()) {
            holder.ivDownvote.setImageResource(R.drawable.ic_downvote_inactive);
            --currentDownvote;
            holder.tvDownvoteAmount.setText(currentDownvote + "");
            deal.setDownvote(currentDownvote);
            deal.setDownvoted(false);
        } else {
            holder.ivDownvote.setImageResource(R.drawable.ic_downvote_active);
            ++currentDownvote;
            holder.tvDownvoteAmount.setText(currentDownvote + "");
            deal.setDownvoted(true);
            deal.setDownvote(currentDownvote);

            int currentUpvote = deal.getUpvote();
            if (deal.isUpvoted()) {
                holder.ivUpvote.setImageResource(R.drawable.ic_upvote_inactive);
                --currentUpvote;
                holder.tvUpvoteAmount.setText(currentUpvote + "");
                deal.setUpvoted(false);
                deal.setUpvote(currentUpvote);
            }
        }
    }

    private void upVote(ItemVoteBinding holder, Deal deal) {
        int currentUpvotes = deal.getUpvote();
        if (deal.isUpvoted()) {
            holder.ivUpvote.setImageResource(R.drawable.ic_upvote_inactive);
            --currentUpvotes;
            holder.tvUpvoteAmount.setText(currentUpvotes + "");
            deal.setUpvote(currentUpvotes);
            deal.setUpvoted(false);
        } else {
            holder.ivUpvote.setImageResource(R.drawable.ic_upvote_active);
            ++currentUpvotes;
            holder.tvUpvoteAmount.setText(currentUpvotes + "");
            deal.setUpvoted(true);
            deal.setUpvote(currentUpvotes);

            int currentDownvote = deal.getDownvote();
            if (deal.isDownvoted()) {
                holder.ivDownvote.setImageResource(R.drawable.ic_downvote_inactive);
                --currentDownvote;
                holder.tvDownvoteAmount.setText(currentDownvote + "");
                deal.setDownvoted(false);
                deal.setDownvote(currentDownvote);
            }
        }
    }

    private void clickDownVote() {
        mCompositeDisposable.add(
                mApi.downVote(mDeal.getId(), UserManager.getUserSync().getAuthorization())
                        .compose(RxUtil.withSchedulers())
                        .subscribe(retrofitResponseItem -> {
                        }, Throwable::printStackTrace));
    }

    private void clickUpvote() {
        mCompositeDisposable.add(
                mApi.upVote(mDeal.getId(), UserManager.getUserSync().getAuthorization())
                        .compose(RxUtil.withSchedulers())
                        .subscribe(retrofitResponseItem -> {
                        }, Throwable::printStackTrace));
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
        ViewUtil.setText(mBinding.layoutVote.tvUpvoteAmount, String.valueOf(mDeal.getUpvote()));
        ViewUtil.setText(mBinding.layoutVote.tvDownvoteAmount, String.valueOf(mDeal.getDownvote()));

        String userId = UserManager.getUserSync().getId();

        if (mDeal.getUpVoters().contains(userId)) {
            mBinding.layoutVote.ivUpvote.setBackgroundResource(R.drawable.ic_upvote_active);
        } else {
            mBinding.layoutVote.ivUpvote.setBackgroundResource(R.drawable.ic_upvote_inactive);
        }

        if (mDeal.getDownVoters().contains(userId)) {
            mBinding.layoutVote.ivDownvote.setBackgroundResource(R.drawable.ic_downvote_active);
        } else {
            mBinding.layoutVote.ivDownvote.setBackgroundResource(R.drawable.ic_downvote_inactive);
        }
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
