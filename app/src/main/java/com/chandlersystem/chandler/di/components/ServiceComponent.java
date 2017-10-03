package com.chandlersystem.chandler.di.components;

import com.chandlersystem.chandler.di.modules.ServiceModule;
import com.chandlersystem.chandler.di.scopes.PerService;
import com.chandlersystem.chandler.services.ChandlerInstanceIDService;

import dagger.Component;

@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    void inject(ChandlerInstanceIDService sevenRewardsInstanceIDService);
}
