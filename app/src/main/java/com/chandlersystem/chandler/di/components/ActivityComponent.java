package com.chandlersystem.chandler.di.components;

import com.chandlersystem.chandler.di.modules.ActivityModule;
import com.chandlersystem.chandler.di.scopes.PerActivity;
import com.chandlersystem.chandler.ui.create_request.SelectCategoryFragment;
import com.chandlersystem.chandler.ui.deal.DealFragment;
import com.chandlersystem.chandler.ui.login.LoginActivity;
import com.chandlersystem.chandler.ui.select_category.SelectCategoryActivity;

import dagger.Component;

/**
 * Inject in every components what use module of both Application and Activity Module
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(LoginActivity loginActivity);

    void inject(DealFragment dealFragment);

    void inject(SelectCategoryFragment selectCategoryFragment);

    void inject(SelectCategoryActivity selectCategoryActivity);
}
