package com.duggan.workflow.client.ui.landingpage;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class LandingPageView extends ViewImpl implements LandingPagePresenter.ILandingPageView {
    interface Binder extends UiBinder<Widget, LandingPageView> {
    }

    @Inject
    LandingPageView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
   
}