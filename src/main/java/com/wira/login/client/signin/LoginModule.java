package com.wira.login.client.signin;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class LoginModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(LoginPresenter.class, LoginPresenter.ILoginView.class, LoginView.class, LoginPresenter.ILoginProxy.class);
    }
}