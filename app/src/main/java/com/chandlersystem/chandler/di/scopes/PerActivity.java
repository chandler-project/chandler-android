package com.chandlersystem.chandler.di.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Objects come with this Qualifier will alive with Activity life cycle
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
