package com.duggan.workflow.client.ui.landingpage;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class LandingPageModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(LandingPagePresenter.class, LandingPagePresenter.ILandingPageView.class, LandingPageView.class, LandingPagePresenter.ILandingPageProxy.class);
    }
}