package com.duggan.workflow.client.ui.admin.users;

import java.util.List;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.adduser.AddUserPresenter;
import com.duggan.workflow.client.ui.admin.adduser.AddUserPresenter.TYPE;
import com.duggan.workflow.client.ui.admin.users.item.UserItemPresenter;
import com.duggan.workflow.client.ui.events.EditUserEvent;
import com.duggan.workflow.client.ui.events.EditUserEvent.EditUserHandler;
import com.duggan.workflow.client.ui.events.LoadUsersEvent;
import com.duggan.workflow.client.ui.events.LoadUsersEvent.LoadUsersHandler;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.responses.GetUsersResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class UserPresenter extends PresenterWidget<UserPresenter.MyView> 
implements EditUserHandler, LoadUsersHandler{

	public interface MyView extends View {

		HasClickHandlers getaNewUser();
		HasClickHandlers getaNewGroup();
	}
	
	public static final Object ITEMSLOT = new Object();
	
	IndirectProvider<AddUserPresenter> userFactory;
	IndirectProvider<UserItemPresenter> userItemFactory;

	TYPE type = TYPE.USER;

	@Inject DispatchAsync requestHelper;
	
	@Inject
	public UserPresenter(final EventBus eventBus, final MyView view,
			Provider<AddUserPresenter> addUserProvider,
			Provider<UserItemPresenter> itemProvider) {
		super(eventBus, view);
		userFactory = new StandardProvider<AddUserPresenter>(addUserProvider);
		userItemFactory = new StandardProvider<UserItemPresenter>(itemProvider);
	}
	
	private void showPopup(final AddUserPresenter.TYPE type){
		showPopup(type, null);
	}
	
	private void showPopup(final AddUserPresenter.TYPE type, final Object obj) {
		
		userFactory.get(new ServiceCallback<AddUserPresenter>() {
			@Override
			public void processResult(AddUserPresenter result) {
				result.setType(type, obj);
				
				addToPopupSlot(result);
			}
		});
			
	}
	

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditUserEvent.TYPE, this);
		addRegisteredHandler(LoadUsersEvent.TYPE, this);
		
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
	
	void loadData(){
		if(type==TYPE.USER){
			loadUsers();
		}else{
			//load Groups
		}
	}
	
	
	
	private void loadUsers() {
		GetUsersRequest request = new GetUsersRequest();
		requestHelper.execute(request, new TaskServiceCallback<GetUsersResponse>() {
			@Override
			public void processResult(GetUsersResponse result) {
				List<HTUser> users = result.getUsers();
				loadUsers(users);
			}
		});
	}

	protected void loadUsers(List<HTUser> users) {
		setInSlot(ITEMSLOT, null);
		if(users!=null)
			for(final HTUser user: users){
				userItemFactory.get(new ServiceCallback<UserItemPresenter>() {
					@Override
					public void processResult(UserItemPresenter result) {
						result.setUser(user);
						addToSlot(ITEMSLOT, result);
					}
				});
			}
	}

	@Override
	public void onEditUser(EditUserEvent event) {
		showPopup(TYPE.USER, event.getUser());
	}

	@Override
	public void onLoadUsers(LoadUsersEvent event) {
		loadData();
	}
}
