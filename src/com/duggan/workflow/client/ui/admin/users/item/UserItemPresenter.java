package com.duggan.workflow.client.ui.admin.users.item;

import com.duggan.workflow.client.ui.events.EditUserEvent;
import com.duggan.workflow.shared.model.HTUser;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class UserItemPresenter extends PresenterWidget<UserItemPresenter.MyView> {

	public interface MyView extends View {
		void setValues(String firstName, String lastName, String email, String groups);
		
		HasClickHandlers getEdit();
		
		HasClickHandlers getDelete();
	}

	HTUser user;
	
	@Inject
	public UserItemPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getEdit().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new EditUserEvent(user));
			}
		});
		
		getView().getDelete().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
			}
		});
	}
	
	public void setUser(HTUser user){
		this.user = user;
		getView().setValues(user.getName(), user.getSurname(), user.getEmail(), user.getGroupsAsString());
	}
}
