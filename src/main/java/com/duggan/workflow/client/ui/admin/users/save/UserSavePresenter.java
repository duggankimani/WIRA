package com.duggan.workflow.client.ui.admin.users.save;

import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.LoadGroupsEvent;
import com.duggan.workflow.client.ui.events.LoadOrganizationsEvent;
import com.duggan.workflow.client.ui.events.LoadUsersEvent;
import com.duggan.workflow.server.actionhandlers.GetAllOrganizationsRequestActionHandler;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Organization;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.requests.GetAllOganizationsRequest;
import com.duggan.workflow.shared.requests.GetGroupsRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveGroupRequest;
import com.duggan.workflow.shared.requests.SaveOrganizationRequest;
import com.duggan.workflow.shared.requests.SaveUserRequest;
import com.duggan.workflow.shared.responses.GetAllOrganizationsResponse;
import com.duggan.workflow.shared.responses.GetGroupsResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveGroupResponse;
import com.duggan.workflow.shared.responses.SaveOrganizationResponse;
import com.duggan.workflow.shared.responses.SaveUserResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class UserSavePresenter extends PresenterWidget<UserSavePresenter.IUserSaveView> {

	public interface IUserSaveView extends PopupView {

		void setType(TYPE type);

		HasClickHandlers getSaveUser();

		HasClickHandlers getSaveGroup();

		HasClickHandlers getSaveOrg();

		boolean isValid();

		HTUser getUser();

		void setUser(HTUser user);

		UserGroup getGroup();

		Organization getOrg();

		void setGroup(UserGroup group);

		void setGroups(List<UserGroup> groups);

		void setOrgs(List<Organization> organizations);

		void setOrganization(Organization org);

	}

	public enum TYPE {
		GROUP, USER, ORGANIZATION
	}

	TYPE type;

	HTUser user;

	UserGroup group;

	Organization organization;

	@Inject
	DispatchAsync requestHelper;

	@Inject
	public UserSavePresenter(final EventBus eventBus, final IUserSaveView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getSaveUser().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (getView().isValid()) {
					HTUser htuser = getView().getUser();
					if (user != null) {
						htuser.setId(user.getId());
					}
					SaveUserRequest request = new SaveUserRequest(htuser);
					requestHelper.execute(request, new TaskServiceCallback<SaveUserResponse>() {
						@Override
						public void processResult(SaveUserResponse result) {
							user = result.getUser();
							getView().setUser(user);
							getView().hide();
							fireEvent(new LoadUsersEvent());
						}
					});
				}
			}
		});

		getView().getSaveGroup().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (getView().isValid()) {
					UserGroup userGroup = getView().getGroup();

					SaveGroupRequest request = new SaveGroupRequest(userGroup);

					requestHelper.execute(request, new TaskServiceCallback<SaveGroupResponse>() {
						@Override
						public void processResult(SaveGroupResponse result) {
							group = result.getGroup();
							getView().setGroup(group);
							fireEvent(new LoadGroupsEvent());
							getView().hide();
						}
					});
				}
			}
		});

		getView().getSaveOrg().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (getView().isValid()) {
					Organization org = getView().getOrg();
					SaveOrganizationRequest request = new SaveOrganizationRequest(org);

					requestHelper.execute(request, new TaskServiceCallback<SaveOrganizationResponse>() {

						@Override
						public void processResult(SaveOrganizationResponse aResponse) {
							organization = aResponse.getOrganization();
							getView().setOrganization(organization);
							fireEvent(new LoadOrganizationsEvent());
							getView().hide();
						}
					});
				}

			}
		});
	}

	@Override
	protected void onReveal() {
		super.onReveal();

		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetGroupsRequest());
		requests.addRequest(new GetAllOganizationsRequest());

		requestHelper.execute(requests, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				GetGroupsResponse gResp = (GetGroupsResponse) aResponse.get(0);
				GetAllOrganizationsResponse oResp = (GetAllOrganizationsResponse) aResponse.get(1);

				List<UserGroup> groups = gResp.getGroups();
				getView().setGroups(groups);

				List<Organization> organizations = oResp.getOrganizations();
				getView().setOrgs(organizations);
			}
		});

	}

	public void setType(TYPE type, Object value) {
		this.type = type;
		getView().setType(type);
		if (value != null) {
			if (type == TYPE.USER) {
				user = (HTUser) value;
				getView().setUser(user);
			} else if (type == TYPE.GROUP) {
				group = (UserGroup) value;
				getView().setGroup(group);
			} else {
				organization = (Organization) value;
			}
		}

	}
}
