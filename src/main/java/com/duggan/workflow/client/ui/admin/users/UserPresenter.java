package com.duggan.workflow.client.ui.admin.users;

import java.util.ArrayList;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.event.CheckboxSelectionEvent.CheckboxSelectionHandler;
import com.duggan.workflow.client.event.ShowMessageEvent;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AlertType;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.admin.users.save.UserSaveView;
import com.duggan.workflow.client.ui.events.EditGroupEvent;
import com.duggan.workflow.client.ui.events.EditGroupEvent.EditGroupHandler;
import com.duggan.workflow.client.ui.events.EditUserEvent;
import com.duggan.workflow.client.ui.events.EditUserEvent.EditUserHandler;
import com.duggan.workflow.client.ui.events.LoadGroupsEvent;
import com.duggan.workflow.client.ui.events.LoadGroupsEvent.LoadGroupsHandler;
import com.duggan.workflow.client.ui.events.LoadUsersEvent;
import com.duggan.workflow.client.ui.events.LoadUsersEvent.LoadUsersHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.events.SearchEvent.SearchHandler;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.client.ui.security.HasPermissionsGateKeeper;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Org;
import com.duggan.workflow.shared.model.SearchFilter;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.requests.GetGroupsRequest;
import com.duggan.workflow.shared.requests.GetOrgsRequest;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveGroupRequest;
import com.duggan.workflow.shared.requests.SaveOrgRequest;
import com.duggan.workflow.shared.requests.SaveUserRequest;
import com.duggan.workflow.shared.responses.GetGroupsResponse;
import com.duggan.workflow.shared.responses.GetOrgsResponse;
import com.duggan.workflow.shared.responses.GetUsersResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveGroupResponse;
import com.duggan.workflow.shared.responses.SaveOrgResponse;
import com.duggan.workflow.shared.responses.SaveUserResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.GatekeeperParams;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class UserPresenter extends
		Presenter<UserPresenter.MyView, UserPresenter.MyProxy> implements
		EditUserHandler, LoadUsersHandler, LoadGroupsHandler, EditGroupHandler,
		CheckboxSelectionHandler, SearchHandler {

	public interface MyView extends View {

		HasClickHandlers getaNewUser();

		HasClickHandlers getaNewGroup();

		void setType(TYPE type);

		void bindUsers(ArrayList<HTUser> users);

		void bindGroups(ArrayList<UserGroup> groups);

		void setUserEdit(boolean value);

		void setGroupEdit(boolean value);

		HasClickHandlers getEditUser();

		HasClickHandlers getDeleteUser();

		HasClickHandlers getEditGroup();

		HasClickHandlers getDeleteGroup();

		HasClickHandlers getNewOrg();

		HasClickHandlers getEditOrg();

		HasClickHandlers getDeleteOrg();

		void bindOrgs(ArrayList<Org> orgs);

		void setOrgEdit(boolean value);
	}

	public static final String ACCESSMGT_CAN_VIEW_ACCESSMGT = "ACCESSMGT_CAN_VIEW_ACCESSMGT";

	@ProxyCodeSplit
	@NameToken({NameTokens.usermgt,NameTokens.usermgtwithparam})
	@UseGatekeeper(HasPermissionsGateKeeper.class)
	@GatekeeperParams({ACCESSMGT_CAN_VIEW_ACCESSMGT})
	public interface MyProxy extends TabContentProxyPlace<UserPresenter> {
	}

	@TabInfo(container = AdminHomePresenter.class)
	static TabData getTabLabel(HasPermissionsGateKeeper gateKeeper) {
		/**
		 * Manually calling gateKeeper.withParams Method.
		 * 
		 * HACK NECESSITATED BY THE FACT THAT Gin injects to different instances of this GateKeeper in 
		 * Presenter.MyProxy->UseGateKeeper & 
		 * getTabLabel(GateKeeper);
		 * 
		 * Test -> 
		 * Window.alert in GateKeeper.canReveal(this+" Params = "+params) Vs 
		 * Window.alert here in getTabLabel.canReveal(this+" Params = "+params) Vs
		 * Window.alert in AbstractTabPanel.refreshTabs(tab.getTabData.getGateKeeper()+" Params = "+params) Vs
		 * 
		 */
		gateKeeper.withParams(new String[]{ACCESSMGT_CAN_VIEW_ACCESSMGT}); 
		return new TabDataExt(TABLABEL, "icon-group", 3,
				gateKeeper);
	}

	public static final Object ITEMSLOT = new Object();
	public static final Object GROUPSLOT = new Object();
	public static final String TABLABEL = "Users and Groups";

	TYPE type = TYPE.USER;

	private Object selectedModel;

	@Inject
	DispatchAsync requestHelper;

	@Inject
	public UserPresenter(final EventBus eventBus, final MyView view,
			MyProxy proxy) {
		super(eventBus, view, proxy, AdminHomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(EditUserEvent.TYPE, this);
		addRegisteredHandler(LoadUsersEvent.TYPE, this);
		addRegisteredHandler(LoadGroupsEvent.TYPE, this);
		addRegisteredHandler(EditGroupEvent.TYPE, this);
		addRegisteredHandler(CheckboxSelectionEvent.getType(), this);
		addRegisteredHandler(SearchEvent.getType(), this);

		getView().getaNewUser().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopup(TYPE.USER);
			}
		});

		getView().getaNewGroup().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopup(TYPE.GROUP);
			}
		});

		getView().getEditUser().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopup(TYPE.USER, selectedModel);
			}
		});

		getView().getDeleteUser().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final HTUser user = (HTUser) selectedModel;
				AppManager.showPopUp(
						"Confirm Delete",
						new HTMLPanel("Do you want to delete user \""
								+ user.getName() + "\""),
						new OnOptionSelected() {

							@Override
							public void onSelect(String name) {
								if (name.equals("Delete")) {
									delete(user);

								}
							}

						}, "Cancel", "Delete");

			}
		});

		getView().getEditGroup().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopup(TYPE.GROUP, selectedModel);
			}
		});

		getView().getDeleteGroup().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final UserGroup group = (UserGroup) selectedModel;
				AppManager.showPopUp("Confirm Delete", new HTMLPanel(
						"Do you want to delete group \"" + group.getName()
								+ "\""), new OnOptionSelected() {

					@Override
					public void onSelect(String name) {
						if (name.equals("Delete")) {
							delete(group);
						}
					}

				}, "Cancel", "Delete");
			}
		});

		getView().getNewOrg().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopup(TYPE.ORG, null);
			}
		});

		getView().getEditOrg().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showPopup(TYPE.ORG, selectedModel);
			}
		});

		getView().getDeleteOrg().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final Org org = (Org) selectedModel;
				AppManager.showPopUp("Confirm Delete", new HTMLPanel(
						"Do you want to delete org \"" + org.getName() + "\""),
						new OnOptionSelected() {

							@Override
							public void onSelect(String name) {
								if (name.equals("Delete")) {
									delete(org);
								}
							}

						}, "Cancel", "Delete");

			}
		});

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		String page = request.getParameter("page", "USER").toUpperCase();
		try {
			type = TYPE.valueOf(page);
		} catch (Exception e) {
		}

		if (type == null) {
			type = TYPE.USER;
		}

		setType(type);
		loadData();
	}

	private void showPopup(final TYPE type) {
		showPopup(type, null);
	}

	private void showPopup(final TYPE type, final Object obj) {
		final UserSaveView view = new  UserSaveView(type,obj);
		AppManager.showPopUp("Edit " + type.displayName(), view, new OptionControl() {

			@Override
			public void onSelect(String name) {
				if (name.equals("Save")) {
					if (view.isValid()) {
						if (type == TYPE.GROUP) {
							UserGroup userGroup = view.getGroup();
							saveGroup(userGroup);
						} else if (type == TYPE.USER) {
							HTUser user = view.getUser();
							saveUser(user);
						} else if (type == TYPE.ORG) {
							Org org = view.getOrg();
							saveOrg(org);
						}
						hide();
					}
				} else {
					hide();
				}
			}

		}, "Save", "Cancel");

	}

	private void saveOrg(Org org) {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new SaveOrgRequest(org));
		fireEvent(new ProcessingEvent());
		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						loadOrgs();
						fireEvent(new ShowMessageEvent(AlertType.SUCCESS, "Organization successfully saved!",true));
						fireEvent(new ProcessingCompletedEvent());
					}
				});
	}

	private void saveUser(HTUser user) {
		SaveUserRequest request = new SaveUserRequest(user);
		requestHelper.execute(request,
				new TaskServiceCallback<SaveUserResponse>() {
					@Override
					public void processResult(SaveUserResponse result) {
						fireEvent(new ShowMessageEvent(AlertType.SUCCESS, "User successfully saved!",true));
						fireEvent(new LoadUsersEvent());
					}
				});
	}

	private void saveGroup(UserGroup userGroup) {
		SaveGroupRequest request = new SaveGroupRequest(userGroup);
		requestHelper.execute(request,
				new TaskServiceCallback<SaveGroupResponse>() {
					@Override
					public void processResult(SaveGroupResponse result) {
						fireEvent(new ShowMessageEvent(AlertType.SUCCESS, "Group successfully saved!",true));
						fireEvent(new LoadGroupsEvent());
					}
				});

	}

	void loadData() {
		if (type == TYPE.USER) {
			loadUsers();
		} else if (type == TYPE.GROUP) {
			loadGroups();
		} else {
			loadOrgs();
		}
	}

	private void loadOrgs() {
		loadOrgs(null, 0, 100);
	}

	private void loadOrgs(String searchTerm, int start, int length) {
		fireEvent(new ProcessingEvent());
		requestHelper.execute(new GetOrgsRequest(searchTerm, start, length),
				new TaskServiceCallback<GetOrgsResponse>() {
					@Override
					public void processResult(GetOrgsResponse aResponse) {
						ArrayList<Org> orgs = aResponse.getOrgs();
						getView().bindOrgs(orgs);
						fireEvent(new ProcessingCompletedEvent());
						fireEvent(new CheckboxSelectionEvent(selectedModel,
								true));
					}
				});
	}

	private void loadGroups() {
		loadGroups(null);
	}
	
	private void loadGroups(String searchTerm) {
		GetGroupsRequest request = new GetGroupsRequest();
		request.setSearchTerm(searchTerm);
		fireEvent(new ProcessingEvent());
		requestHelper.execute(request,
				new TaskServiceCallback<GetGroupsResponse>() {
					@Override
					public void processResult(GetGroupsResponse result) {
						ArrayList<UserGroup> groups = result.getGroups();
						getView().bindGroups(groups);
						// loadGroups(groups);
						fireEvent(new ProcessingCompletedEvent());
						fireEvent(new CheckboxSelectionEvent(selectedModel,
								true));
					}
				});
	}

	private void loadUsers() {
		loadUsers(null);
	}
	
	private void loadUsers(String searchTerm) {
		GetUsersRequest request = new GetUsersRequest(searchTerm);
		fireEvent(new ProcessingEvent());
		requestHelper.execute(request,
				new TaskServiceCallback<GetUsersResponse>() {
					@Override
					public void processResult(GetUsersResponse result) {
						ArrayList<HTUser> users = result.getUsers();
						getView().bindUsers(users);
						// loadUsers(users);
						fireEvent(new ProcessingCompletedEvent());
						fireEvent(new CheckboxSelectionEvent(selectedModel,
								true));
					}
				});
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
		this.type = type;
		getView().setType(type);
	}

	@Override
	public void onEditGroup(EditGroupEvent event) {
		showPopup(type, event.getGroup());
	}

	@Override
	public void onCheckboxSelection(CheckboxSelectionEvent event) {

		selectedModel = event.getModel();
		selectItem(selectedModel, event.getValue());

		if (!event.getValue()) {
			selectedModel = null;
		}
	}

	private void selectItem(Object model, boolean value) {

		if (model instanceof HTUser) {
			getView().setUserEdit(value);
		} else if (model instanceof UserGroup) {
			getView().setGroupEdit(value);
		} else if (model instanceof Org) {
			getView().setOrgEdit(value);
		}

	}

	private void delete(HTUser user) {

		SaveUserRequest request = new SaveUserRequest(user);
		request.setDelete(true);
		requestHelper.execute(request,
				new TaskServiceCallback<SaveUserResponse>() {
					@Override
					public void processResult(SaveUserResponse result) {
						fireEvent(new ShowMessageEvent(AlertType.SUCCESS, "User successfully deleted.",false));
						loadUsers();
					}
				});
	}

	protected void delete(UserGroup group) {
		SaveGroupRequest request = new SaveGroupRequest(group);
		request.setDelete(true);
		requestHelper.execute(request,
				new TaskServiceCallback<SaveGroupResponse>() {
					@Override
					public void processResult(SaveGroupResponse result) {
						fireEvent(new ShowMessageEvent(AlertType.SUCCESS, "Group successfully deleted.",false));
						loadGroups();
					}
				});
	}

	protected void delete(Org org) {
		SaveOrgRequest request = new SaveOrgRequest(org);
		request.setDelete(true);
		requestHelper.execute(request,
				new TaskServiceCallback<SaveOrgResponse>() {
					@Override
					public void processResult(SaveOrgResponse result) {
						fireEvent(new ShowMessageEvent(AlertType.SUCCESS, "Organization successfully deleted.",false));
						loadOrgs();
					}
				});
	}
	
	@Override
	public void onSearch(SearchEvent event) {
		if(isVisible()){
			SearchFilter filter = event.getFilter();
			
			switch (type) {
			case GROUP:
				loadGroups(filter.getPhrase());
				break;
			case ORG:
				loadOrgs(filter.getPhrase(),0,100);
				break;
			case USER:
				loadUsers(filter.getPhrase());
				break;
			}
			filter.getPhrase();
		}
	}
}
