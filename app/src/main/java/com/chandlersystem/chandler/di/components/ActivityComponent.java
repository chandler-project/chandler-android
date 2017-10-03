package com.chandlersystem.chandler.di.components;

import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.di.scopes.PerActivity;
import com.chandlersystem.chandler.ui.login.LoginActivity;

import dagger.Component;

/**
 * Inject in every components what use module of both Application and Activity Module
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(LoginActivity loginActivity);
}
