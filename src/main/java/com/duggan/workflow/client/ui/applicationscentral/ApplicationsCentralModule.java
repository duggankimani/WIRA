package com.duggan.workflow.client.ui.applicationscentral;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationsCentralModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ApplicationsCentralPresenter.class, ApplicationsCentralPresenter.MyView.class, ApplicationsCentralView.class, ApplicationsCentralPresenter.MyProxy.class);
    }
}