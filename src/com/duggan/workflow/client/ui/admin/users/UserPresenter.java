package com.duggan.workflow.client.ui.admin.users;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.ui.admin.adduser.AddUserPresenter;
import com.duggan.workflow.client.ui.admin.adduser.AddUserPresenter.TYPE;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class UserPresenter extends PresenterWidget<UserPresenter.MyView> {

	public interface MyView extends View {

		HasClickHandlers getaNewUser();
		HasClickHandlers getaNewGroup();
	}
	
	IndirectProvider<AddUserPresenter> userFactory;

	@Inject
	public UserPresenter(final EventBus eventBus, final MyView view,Provider<AddUserPresenter> addUserProvider ) {
		super(eventBus, view);
		userFactory = new StandardProvider<AddUserPresenter>(addUserProvider);
		
		getView().getaNewUser().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopup(TYPE.USER);
			}
		});
		
		getView().getaNewGroup().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopup(AddUserPresenter.TYPE.GROUP);
			}
		});
	}
	
	private void showPopup(final AddUserPresenter.TYPE type) {
		userFactory.get(new ServiceCallback<AddUserPresenter>() {
			@Override
			public void processResult(AddUserPresenter result) {
				result.setType(type);
				addToPopupSlot(result,false);
			}
		});
			
	}
	

	@Override
	protected void onBind() {
		super.onBind();
	}
}
