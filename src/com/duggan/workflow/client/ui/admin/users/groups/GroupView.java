package com.duggan.workflow.client.ui.admin.users.groups;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class GroupView extends ViewImpl implements GroupPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, GroupView> {
	}

	@UiField HTMLPanel panelFirstName;
	@UiField HTMLPanel panelLastName;
	
	@UiField Anchor aEdit;
	@UiField Anchor aDelete;
	
	@Inject
	public GroupView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	public void setValues(String code, String name){
		
		if(code!=null){
			panelFirstName.getElement().setInnerText(code);
		}

		if(name!=null){
			panelLastName.getElement().setInnerText(name);
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
