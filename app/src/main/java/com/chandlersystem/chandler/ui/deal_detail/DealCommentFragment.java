package com.chandlersystem.chandler.ui.deal_detail;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chandlersystem.chandler.ChandlerApplication;
import com.chandlersystem.chandler.GlideApp;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.RxBus;
import com.chandlersystem.chandler.custom_views.LinearItemDecoration;
import com.chandlersystem.chandler.data.api.ChandlerApi;
import com.chandlersystem.chandler.data.models.pojo.Comment;
import com.chandlersystem.chandler.data.models.pojo.Deal;
import com.chandlersystem.chandler.data.models.pojo.FileUpload;
import com.chandlersystem.chandler.data.models.pojo.UploadImage;
import com.chandlersystem.chandler.data.models.request.CommentBody;
import com.chandlersystem.chandler.data.models.response.RetrofitResponseItem;
import com.chandlersystem.chandler.database.UserManager;
import com.chandlersystem.chandler.databinding.FragmentDealCommentBinding;
import com.chandlersystem.chandler.di.components.ActivityComponent;
import com.chandlersystem.chandler.di.components.DaggerActivityComponent;
import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.ui.adapters.CommentAdapter;
import com.chandlersystem.chandler.ui.adapters.ItemImageAdapter;
import com.chandlersystem.chandler.utilities.DialogUtil;
import com.chandlersystem.chandler.utilities.RxUtil;
import com.chandlersystem.chandler.utilities.ValidateUtil;
import com.chandlersystem.chandler.utilities.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class DealCommentFragment extends Fragment {
    private static final String ARGUMENT_DEAL = "argument-deal";
    private static final int REQUEST_CODE_PICK_PHOTO = 999;

    private FragmentDealCommentBinding mBinding;

    private CommentAdapter mUserCommentAdapter;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private String mImagePath;

    private Deal mDeal;

    @Inject
    ChandlerApi mApi;

    public DealCommentFragment() {
        // Required empty public constructor
    }

    public static DealCommentFragment newInstance(Deal deal) {
        DealCommentFragment fragment = new DealCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_DEAL, deal);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_deal_comment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDeal = getArguments().getParcelable(ARGUMENT_DEAL);
        setupRecyclerView();
        setupButton();
        handleEvents();
        getCommentApi();
    }

    private void getCommentApi() {
        mCompositeDisposable.add(mApi.getComment(mDeal.getId(), UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(commentRetrofitResponseListItem -> setAdapter(commentRetrofitResponseListItem.items), Throwable::printStackTrace));
    }

    private void setupButton() {
        mBinding.layoutCommentButton.btnPrimary.setText(getText(R.string.content_button_comment));
    }

    private void handleEvents() {
        mCompositeDisposable.add(RxView.clicks(mBinding.layoutCommentImage.layoutAddImage)
                .subscribe(o -> startImagePicker(), Throwable::printStackTrace));

        mCompositeDisposable.add(RxView.clicks(mBinding.layoutCommentImage.ivRemove)
                .subscribe(o -> hideImage(), Throwable::printStackTrace));

        mCompositeDisposable.add(textChanges()
                .subscribe(this::enableButton, Throwable::printStackTrace));

        mCompositeDisposable.add(RxView.clicks(mBinding.layoutCommentButton.btnPrimary)
                .subscribe(o -> callApiPostComment(), Throwable::printStackTrace));
    }

    private void enableButton(Boolean b) {
        mBinding.layoutCommentButton.btnPrimary.setEnabled(b);
        if (b) {
            mBinding.layoutCommentButton.btnPrimary.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        } else {
            mBinding.layoutCommentButton.btnPrimary.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorInactive));
        }
    }

    private void callApiPostComment() {
        CommentBody commentBody = new CommentBody();
        commentBody.setContent(mBinding.etComment.getText().toString());
        if (ValidateUtil.checkString(mImagePath)) {

            List<String> imagePathList = new ArrayList<>();
            imagePathList.add(mImagePath);
            List<MultipartBody.Part> filePartList = new ArrayList<>();
            for (String imagePath : imagePathList) {
                MultipartBody.Part part = getRequestBody(new File(getRealPathFromURI(imagePath)));
                filePartList.add(part);
            }

            mCompositeDisposable.add(mApi.uploadImages(UserManager.getUserSync().getAuthorization(), filePartList)
                    .compose(RxUtil.withSchedulers())
                    .doOnSubscribe(disposable -> ViewUtil.toggleView(mBinding.layoutProgressBar.progressBar, true))
                    .doOnTerminate(() -> ViewUtil.toggleView(mBinding.layoutProgressBar.progressBar, false))
                    .subscribe(responseItem -> {
                        List<String> urlList = new ArrayList<>();
                        for (FileUpload fileUpload : responseItem.item.getResult().getFiles().getFileUpload()) {
                            urlList.add("http://etylash.xyz:3001/" + fileUpload.getName());
                        }

                        commentBody.setImages(urlList);
                        postCommentApi(commentBody);
                    }, throwable -> {
                        DialogUtil.showErrorDialog(getContext(), throwable);
                    }));
        } else {
            postCommentApi(commentBody);
        }
    }

    private void postCommentApi(CommentBody commentBody) {
        mCompositeDisposable.add(mApi.postComment(commentBody, mDeal.getId(), UserManager.getUserSync().getAuthorization())
                .compose(RxUtil.withSchedulers())
                .subscribe(retrofitResponseListItem -> {
                    Toast.makeText(getContext(), getString(R.string.content_post_comment_success), Toast.LENGTH_SHORT).show();
                    resetView();
                    getCommentApi();
                    ViewUtil.hideSoftKey(getActivity());
                }, throwable -> DialogUtil.showErrorDialog(getContext(), throwable)));
    }

    private void resetView() {
        mBinding.etComment.setText("");
        hideImage();
    }

    private MultipartBody.Part getRequestBody(File imageFile) {
        RequestBody request = RequestBody.create(MediaType.parse("image/*"), imageFile);

        return MultipartBody.Part.createFormData("fileUpload", imageFile.getName(), request);
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    private Observable<Boolean> textChanges() {
        return RxTextView.textChanges(mBinding.etComment).map(charSequence -> charSequence.toString().length() > 0);
    }

    private void hideImage() {
        mImagePath = null;
        ViewUtil.toggleView(mBinding.layoutCommentImage.layoutRemoveImage, false);
        ViewUtil.toggleView(mBinding.layoutCommentImage.layoutAddImage, true);
    }

    public void startImagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_PICK_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_PHOTO: {
                if (resultCode == RESULT_OK) {
                    String imagePath = data.getData().toString();
                    setupRemoveImageView(imagePath);
                }
                break;
            }
        }
    }

    private void setupRemoveImageView(String imagePath) {
        boolean isImagePathValidated = ValidateUtil.checkString(imagePath);
        ViewUtil.toggleView(mBinding.layoutCommentImage.layoutRemoveImage, true);
        ViewUtil.toggleView(mBinding.layoutCommentImage.layoutAddImage, false);

        if (isImagePathValidated) {
            Uri imagePathUri = Uri.parse(imagePath);
            GlideApp.with(getContext())
                    .load(imagePathUri)
                    .into(mBinding.layoutCommentImage.ivPhoto);
            ViewUtil.setText(mBinding.layoutCommentImage.tvImageName, imagePath);
            mImagePath = imagePath;
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewComments.setLayoutManager(layoutManager);
        mBinding.recyclerViewComments.setNestedScrollingEnabled(true);
        mBinding.recyclerViewComments.setHasFixedSize(true);
        mBinding.recyclerViewComments.addItemDecoration(new LinearItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_small)));
        mBinding.recyclerViewComments.setEmptyView(mBinding.layoutEmpty.layoutEmpty);
        mBinding.layoutEmpty.tvEmpty.setText(getText(R.string.content_there_is_no_comment));
        ViewUtil.setImage(mBinding.layoutEmpty.ivEmpty, R.drawable.ic_empty_comment);
    }

    private void setAdapter(List<Comment> commentList) {
        mUserCommentAdapter = new CommentAdapter(getContext(), commentList);
        mBinding.recyclerViewComments.setAdapter(mUserCommentAdapter);
    }

}
