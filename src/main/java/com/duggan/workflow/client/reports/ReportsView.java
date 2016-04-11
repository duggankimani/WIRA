package com.duggan.workflow.client.reports;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class ReportsView extends ViewImpl implements ReportsPresenter.MyView {
    interface Binder extends UiBinder<Widget, ReportsView> {
    }

    @UiField
    SimplePanel main;

    @Inject
    ReportsView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}