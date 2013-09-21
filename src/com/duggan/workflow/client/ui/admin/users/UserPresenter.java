package com.duggan.workflow.client.ui.admin.users;

import java.util.List;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.adduser.AddUserPresenter;
import com.duggan.workflow.client.ui.admin.adduser.AddUserPresenter.TYPE;
import com.duggan.workflow.client.ui.admin.users.groups.GroupPresenter;
import com.duggan.workflow.client.ui.admin.users.item.UserItemPresenter;
import com.duggan.workflow.client.ui.events.EditGroupEvent;
import com.duggan.workflow.client.ui.events.EditUserEvent;
import com.duggan.workflow.client.ui.events.LoadGroupsEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.EditGroupEvent.EditGroupHandler;
import com.duggan.workflow.client.ui.events.EditUserEvent.EditUserHandler;
import com.duggan.workflow.client.ui.events.LoadUsersEvent;
import com.duggan.workflow.client.ui.events.LoadGroupsEvent.LoadGroupsHandler;
import com.duggan.workflow.client.ui.events.LoadUsersEvent.LoadUsersHandler;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.requests.GetGroupsRequest;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.responses.GetGroupsResponse;
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
implements EditUserHandler, LoadUsersHandler, LoadGroupsHandler, EditGroupHandler{

	public interface MyView extends View {

		HasClickHandlers getaNewUser();
		HasClickHandlers getaNewGroup();
		void setType(TYPE type);
	}
	
	public static final Object ITEMSLOT = new Object();
	public static final Object GROUPSLOT = new Object();
	
	IndirectProvider<AddUserPresenter> userFactory;
	IndirectProvider<UserItemPresenter> userItemFactory;
	IndirectProvider<GroupPresenter> groupFactory;

	TYPE type = TYPE.USER;

	@Inject DispatchAsync requestHelper;
	
	@Inject
	public UserPresenter(final EventBus eventBus, final MyView view,
			Provider<AddUserPresenter> addUserProvider,
			Provider<UserItemPresenter> itemProvider,
			Provider<GroupPresenter> groupProvider) {
		super(eventBus, view);
		userFactory = new StandardProvider<AddUserPresenter>(addUserProvider);
		userItemFactory = new StandardProvider<UserItemPresenter>(itemProvider);
		groupFactory = new StandardProvider<GroupPresenter>(groupProvider); 
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
		addRegisteredHandler(LoadGroupsEvent.TYPE, this);
		addRegisteredHandler(EditGroupEvent.TYPE, this);
		
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
			loadGroups();
		}
	}
	
	private void loadGroups() {
		GetGroupsRequest request = new GetGroupsRequest();
		fireEvent(new ProcessingEvent());
		requestHelper.execute(request, new TaskServiceCallback<GetGroupsResponse>() {
			@Override
			public void processResult(GetGroupsResponse result) {
				List<UserGroup> groups = result.getGroups();
				loadGroups(groups);
				fireEvent(new ProcessingCompletedEvent());
			}
		});
	}

	protected void loadGroups(List<UserGroup> groups) {
		setInSlot(GROUPSLOT, null);
		for(final UserGroup group: groups){
			groupFactory.get(new ServiceCallback<GroupPresenter>() {
				@Override
				public void processResult(GroupPresenter result) {
					result.setGroup(group);
					addToSlot(GROUPSLOT, result);
				}
			});
		}
		
	}

	private void loadUsers() {
		GetUsersRequest request = new GetUsersRequest();
		fireEvent(new ProcessingEvent());
		requestHelper.execute(request, new TaskServiceCallback<GetUsersResponse>() {
			@Override
			public void processResult(GetUsersResponse result) {
				List<HTUser> users = result.getUsers();
				loadUsers(users);
				fireEvent(new ProcessingCompletedEvent());
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
	
	@Override
	public void onLoadGroups(LoadGroupsEvent event) {
		loadData();
	}

	public void setType(TYPE type) {
		this.type=type;
		getView().setType(type);
	}

	@Override
	public void onEditGroup(EditGroupEvent event) {
		showPopup(type, event.getGroup());
	}
}
