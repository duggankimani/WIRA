package com.duggan.workflow.client.ui.admin.users.item;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.EditUserEvent;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.requests.SaveUserRequest;
import com.duggan.workflow.shared.responses.SaveUserResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class UserItemPresenter extends PresenterWidget<UserItemPresenter.MyView> {

	public interface MyView extends View {
		void setValues(String firstName, String lastName, String username, String email, String groups);
		
		HasClickHandlers getEdit();
		
		HasClickHandlers getDelete();
	}

	HTUser user;
	
	@Inject DispatchAsync requestHelper;
	
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
				SaveUserRequest request = new SaveUserRequest(user);
				request.setDelete(true);
				requestHelper.execute(request, new TaskServiceCallback<SaveUserResponse>() {
					@Override
					public void processResult(SaveUserResponse result) {
						getView().asWidget().removeFromParent();
					}
				});
			}
		});
	}
	
	public void setUser(HTUser user){
		this.user = user;
		getView().setValues(user.getName(), user.getSurname(), user.getUserId(), user.getEmail(), user.getGroupsAsString());
	}
}
