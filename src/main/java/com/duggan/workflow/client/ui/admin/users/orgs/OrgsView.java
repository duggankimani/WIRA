package com.duggan.workflow.client.ui.admin.users.orgs;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class OrgsView extends ViewImpl implements OrgsPresenter.MyView {
    interface Binder extends UiBinder<Widget, OrgsView> {
    }
    
    private final Widget widget;

    @UiField HTMLPanel panelName;
	@UiField HTMLPanel panelFullName;
	
	@UiField Anchor aEdit;
	@UiField Anchor aDelete;
	
	@Inject
	public OrgsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	public void setValues(String code, String name){
		
		if(code!=null){
			panelName.getElement().setInnerText(code);
		}

		if(name!=null){
			panelFullName.getElement().setInnerText(name);
		}
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HasClickHandlers getEdit(){
		return aEdit;
	}
	
	public HasClickHandlers getDelete(){
		return aDelete;
	}
    
}