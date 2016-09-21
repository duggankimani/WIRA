package com.duggan.workflow.client.ui.applicationscentral;

import javax.inject.Inject;

import com.duggan.workflow.shared.model.Doc;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class ApplicationsCentralView extends ViewImpl implements ApplicationsCentralPresenter.MyView {
    interface Binder extends UiBinder<Widget, ApplicationsCentralView> {
    }
    
    @UiField HTMLPanel docContainer;
    
    @UiField Element spnProcessName;
    @UiField Element spnCaseNumber;
    
    @Inject
    ApplicationsCentralView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        bindSlot(ApplicationsCentralPresenter.DOCUMENT_SLOT, docContainer);
    }

	@Override
	public void setContext(Doc doc) {
		String name = doc.getProcessName();
		String caseNumber = doc.getCaseNo();
		
		spnProcessName.setInnerText(name);
		spnCaseNumber.setInnerText(caseNumber);
	}
    
}