package com.chandlersystem.chandler.ui.product_detail;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.databinding.FragmentDealInforBinding;
import com.chandlersystem.chandler.ui.adapters.ImagePagerAdapter;
import com.chandlersystem.chandler.utilities.TextUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class DealInforFragment extends Fragment {
    private FragmentDealInforBinding mBinding;

    private List<String> mProductImageList;
    private ImagePagerAdapter mImagePagerAdapter;

    public DealInforFragment() {
        // Required empty public constructor
    }

    public static DealInforFragment newInstance() {
        return new DealInforFragment();
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
        setupImageSlideShow();
        setDealTitle();
        setDealOwnerName();
        setProfile();
        setPrice();
        setEndDealDate();
        setReactionAmount();
        setLinkPreview();
        setDealDetail();
    }

    private void setDealDetail() {
        mBinding.tvDealDetail.setText(Html.fromHtml("<h2>What is Lorem Ipsum?</h2>\n" +
                "<p><strong>Lorem Ipsum</strong> is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.</p>\n" +
                "</div><div>\n" +
                "<h2>Why do we use it?</h2>\n" +
                "<p>It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).</p>\n" +
                "</div><br /><div>\n" +
                "<h2>Where does it come from?</h2>\n" +
                "<p>Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.</p><p>The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.</p>\n" +
                "</div><div>\n" +
                "<h2>Where can I get some?</h2>\n" +
                "<p>There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.</p>"));
    }

    private void setLinkPreview() {
        mBinding.layoutLinkPreview.ivWebIcon.setImageResource(R.drawable.ic_app);
        mBinding.layoutLinkPreview.tvLink.setText("https://developer.android.com/training/testing/fundamentals.html");
        mBinding.layoutLinkPreview.tvDescription.setText(Html.fromHtml("<h2>What is Lorem Ipsum?</h2>\n" +
                "<p><strong>Lorem Ipsum</strong> is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.</p>\n" +
                "</div><div>"));
        ViewUtil.showImage(getContext(), "http://lorempixel.com/400/200/sports/6/", mBinding.layoutLinkPreview.ivWebPreview);
    }

    private void setReactionAmount() {
        mBinding.layoutVote.tvUpvoteAmount.setText(String.valueOf(12));
        mBinding.layoutVote.tvDownvoteAmount.setText(String.valueOf(12));
        mBinding.tvShareAmount.setText(String.valueOf(24));
    }

    private void setEndDealDate() {
        mBinding.tvEndDate.setText("27/02/2018");
    }

    private void setPrice() {
        mBinding.tvPrice.setText("$200");
    }

    private void setProfile() {
        ViewUtil.toggleView(mBinding.layoutProfile.tvTitle, false);
        ViewUtil.showImage(getContext(), "http://lorempixel.com/40/40/sports/1/", mBinding.layoutProfile.ivProfile);
    }

    private void setDealOwnerName() {
        mBinding.tvOwner.setText("NHP");
    }

    private void setDealTitle() {
        mBinding.tvDealTitle.setText("This is the title");
    }

    private void setupImageSlideShow() {
        mProductImageList = new ArrayList<>();
        mProductImageList.add("http://lorempixel.com/400/200/sports/1/");
        mProductImageList.add("http://lorempixel.com/400/200/sports/2/");
        mProductImageList.add("http://lorempixel.com/400/200/sports/3/");
        mProductImageList.add("http://lorempixel.com/400/200/sports/4/");
        mProductImageList.add("http://lorempixel.com/400/200/sports/5/");
        mProductImageList.add("http://lorempixel.com/400/200/sports/6/");
        mImagePagerAdapter = new ImagePagerAdapter(getContext(), mProductImageList);
        mBinding.viewpagerSlideShow.setAdapter(mImagePagerAdapter);
        mBinding.viewpagerSlideShow.setOffscreenPageLimit(3);
        mBinding.indicator.setViewPager(mBinding.viewpagerSlideShow);
    }
}
