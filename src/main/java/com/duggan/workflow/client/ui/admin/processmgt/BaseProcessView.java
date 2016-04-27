package com.duggan.workflow.client.ui.admin.processmgt;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class BaseProcessView extends ViewImpl implements BaseProcessPresenter.MyView {
    interface Binder extends UiBinder<Widget, BaseProcessView> {
    }

    @UiField
    SimplePanel main;

    @Inject
    BaseProcessView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        bindSlot(BaseProcessPresenter.CONTENT_SLOT, main);
    }
    
}