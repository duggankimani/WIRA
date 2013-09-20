package com.duggan.workflow.client.ui.admin.users.item;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class UserItemView extends ViewImpl implements UserItemPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, UserItemView> {
	}

	@UiField HTMLPanel panelFirstName;
	@UiField HTMLPanel panelLastName;
	@UiField HTMLPanel panelEmail;
	@UiField HTMLPanel panelGroups;
	
	@UiField Anchor aEdit;
	@UiField Anchor aDelete;
	
	@Inject
	public UserItemView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	public void setValues(String firstName, String lastName, String email, String groups){
		
		if(firstName!=null){
			panelFirstName.getElement().setInnerText(firstName);
		}

		if(lastName!=null){
			panelLastName.getElement().setInnerText(lastName);
		}
		
		if(email!=null){
			panelEmail.getElement().setInnerText(email);
		}
		
		if(groups!=null){
			panelGroups.getElement().setInnerText(groups);
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
