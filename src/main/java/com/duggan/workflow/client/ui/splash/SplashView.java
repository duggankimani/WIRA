package com.duggan.workflow.client.ui.splash;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class SplashView extends ViewImpl implements SplashPresenter.MyView {
    interface Binder extends UiBinder<Widget, SplashView> {
    }

    @Inject
    SplashView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
}