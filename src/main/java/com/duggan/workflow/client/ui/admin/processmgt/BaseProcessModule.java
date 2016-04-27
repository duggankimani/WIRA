package com.duggan.workflow.client.ui.admin.processmgt;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class BaseProcessModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(BaseProcessPresenter.class, BaseProcessPresenter.MyView.class, BaseProcessView.class, BaseProcessPresenter.MyProxy.class);
    }
}