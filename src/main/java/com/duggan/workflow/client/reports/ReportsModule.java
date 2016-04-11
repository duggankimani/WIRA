package com.duggan.workflow.client.reports;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ReportsModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ReportsPresenter.class, ReportsPresenter.MyView.class, ReportsView.class, ReportsPresenter.MyProxy.class);
    }
}