package com.duggan.workflow.client.ui.splash;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SplashModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(SplashPresenter.class, SplashPresenter.MyView.class, SplashView.class, SplashPresenter.MyProxy.class);
    }
}