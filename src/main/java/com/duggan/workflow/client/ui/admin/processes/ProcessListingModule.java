package com.duggan.workflow.client.ui.admin.processes;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ProcessListingModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ProcessListingPresenter.class, ProcessListingPresenter.MyView.class, ProcessListingView.class, ProcessListingPresenter.MyProxy.class);
    }
}