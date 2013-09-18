package com.duggan.workflow.client.ui.admin.users;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class UserView extends ViewImpl implements UserPresenter.MyView {

	private final Widget widget;
	@UiField Anchor aNewUser;
	@UiField Anchor aNewGroup;
	@UiField Anchor aUserstab;
	@UiField Anchor aGroupstab;
	

	public interface Binder extends UiBinder<Widget, UserView> {
	}

	@Inject
	public UserView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		aUserstab.getElement().setAttribute("data-toggle", "tab");
		aGroupstab.getElement().setAttribute("data-toggle", "tab");
		
		aUserstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			  aNewUser.removeStyleName("hide");
			  aNewGroup.addStyleName("hide");
			}
		});
		
		aGroupstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			  aNewUser.addStyleName("hide");
			  aNewGroup.removeStyleName("hide");
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasClickHandlers getaNewUser() {
		return aNewUser;
	}
	
	@Override
	public HasClickHandlers getaNewGroup() {
		return aNewGroup;
	}
	
}
