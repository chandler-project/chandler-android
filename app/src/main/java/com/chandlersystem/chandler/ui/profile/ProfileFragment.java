package com.chandlersystem.chandler.ui.profile;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chandlersystem.chandler.GlideApp;
import com.chandlersystem.chandler.R;
import com.chandlersystem.chandler.database.DatabaseHelper;
import com.chandlersystem.chandler.databinding.FragmentProfileBinding;
import com.chandlersystem.chandler.ui.login.LoginActivity;
import com.chandlersystem.chandler.utilities.CircleTransform;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class ProfileFragment extends Fragment {
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private FragmentProfileBinding mBinding;

    @NonNull
    public static Fragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        GlideApp.with(this).load(R.drawable.avatar)
                .transform(new CircleTransform(getContext()))
                .into(mBinding.layoutContent.imageView);

        Fragment reviewsFragment = ReviewsFragment.newInstance(1);
        getChildFragmentManager().beginTransaction().add(R.id.review_list_container, reviewsFragment).commit();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleEvents();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCompositeDisposable.clear();
    }

    private void handleEvents() {
        mCompositeDisposable.add(buttonEditClicks()
                .subscribe(o -> startEditProfileActivity(), Throwable::printStackTrace));

        mCompositeDisposable.add(signOut().subscribe(o -> {
            DatabaseHelper.clearDatabase(getContext());
            showLogoutDialog();
        }, Throwable::printStackTrace));
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.content_logout_title))
                .setMessage(getString(R.string.content_logout_content))
                .setPositiveButton(getString(R.string.content_yes), (dialogInterface, i) -> startLoginActivity())
                .setNegativeButton(getString(R.string.content_no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create();
        builder.show();
    }

    private void startLoginActivity() {
        getActivity().finish();
        startActivity(LoginActivity.getInstance(getContext()));
    }

    private Observable<Object> signOut() {
        return RxView.clicks(mBinding.layoutContent.tvSignOut);
    }

    private void startEditProfileActivity() {
        startActivity(EditProfileActivity.getInstance(getContext()));
    }

    private Observable<Object> buttonEditClicks() {
        return RxView.clicks(mBinding.layoutContent.ivEdit);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
