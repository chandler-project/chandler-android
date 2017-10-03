package com.chandlersystem.chandler.ui.login;

import android.content.Intent;

import com.chandlersystem.chandler.di.scopes.PerActivity;

@PerActivity
public interface LoginPresenter {

    void onActivityLaunched();

    void onDetach();

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
