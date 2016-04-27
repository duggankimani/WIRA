package com.duggan.workflow.client.ui.admin.users.save;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.users.TYPE;
import com.duggan.workflow.client.ui.component.AutoCompleteField;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.PasswordField;
import com.duggan.workflow.client.ui.component.TextArea;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Module;
import com.duggan.workflow.shared.model.Org;
import com.duggan.workflow.shared.model.PermissionPOJO;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.requests.GetGroupsRequest;
import com.duggan.workflow.shared.requests.GetOrgsRequest;
import com.duggan.workflow.shared.requests.GetPermissionsRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetGroupsResponse;
import com.duggan.workflow.shared.responses.GetOrgsResponse;
import com.duggan.workflow.shared.responses.GetPermissionsResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserSaveView extends Composite {

	@UiField
	HTMLPanel divUserDetails;
	@UiField
	HTMLPanel divGroupDetails;
	@UiField
	IssuesPanel issues;

	@UiField
	TextField txtUserName;
	@UiField
	TextField txtFirstname;
	@UiField
	TextField txtLastname;
	@UiField
	TextField txtEmail;
	@UiField
	PasswordField txtPassword;
	@UiField
	PasswordField txtConfirmPassword;

	@UiField
	TextField txtGroupname;
	@UiField
	TextArea txtDescription;
	// @UiField TextField txtUsers;

	@UiField
	Anchor aSaveGroup;
	@UiField
	Anchor aSaveUser;

	@UiField
	DivElement divUserSave;
	@UiField
	Uploader uploader;
	// @UiField ListField<UserGroup> lstGroups;
	@UiField
	AutoCompleteField<UserGroup> lstGroups;

	@UiField
	HTMLPanel divUnitDetails;

	@UiField
	DropDownList<Org> lstOrg;

	@UiField
	FlexTable tblPermissions;
	// Organization/ unit
	@UiField
	TextField txtUnitName;

	@UiField
	Element divPermissions;

	TYPE type;

	private UserGroup group;
	private Org org;


	private static Binder binder = GWT
			.create(Binder.class);
	
	public interface Binder extends UiBinder<Widget, UserSaveView> {
	}

	public UserSaveView() {
		initWidget(binder.createAndBindUi(this));
		
		txtUserName.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				setContext(event.getValue());
			}
		});
	}

	public UserSaveView(TYPE type, Object dto) {
		this();
		this.type = type;
		setType(type);
		if (dto != null) {
			if (dto instanceof HTUser) {
				setUser((HTUser) dto);
				loadPermissions();
			}
			if (dto instanceof UserGroup) {
				setGroup((UserGroup) dto);
			}

			if (dto instanceof Org) {
				setOrg((Org) dto);
			}
		}

		if (type == TYPE.GROUP) {
			loadPermissions();
		} else if (type == TYPE.USER) {
			loadGroups();
		}

	}

	private void loadGroups() {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetGroupsRequest());
		action.addRequest(new GetOrgsRequest(null, 0, 100));

		AppContext.getDispatcher().execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						GetGroupsResponse getGroups = (GetGroupsResponse) aResponse
								.get(0);
						lstGroups.setValues(getGroups.getGroups());
						if (user != null && user.getGroups() != null) {
							lstGroups.select(user.getGroups());
						}

						GetOrgsResponse getOrgs = (GetOrgsResponse) aResponse
								.get(1);
						lstOrg.setItems(getOrgs.getOrgs());
						if (user != null && user.getOrg() != null) {
							lstOrg.setValue(user.getOrg());
						}
					}
				});
	}

	private void loadPermissions() {

		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetPermissionsRequest());

		final String userId = user == null ? null : user.getUserId();
		final String roleName = group == null ? null : group.getName();

		if (userId != null || roleName != null) {
			action.addRequest(new GetPermissionsRequest(userId, roleName));
		}

		AppContext.getDispatcher().execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult aResponse) {
						GetPermissionsResponse getPermissions = (GetPermissionsResponse) aResponse
								.get(0);
						List<PermissionPOJO> permissions = getPermissions
								.getPermissions();

						if (userId != null || roleName != null) {
							GetPermissionsResponse groupPermissionsResp = (GetPermissionsResponse) aResponse
									.get(1);
							List<PermissionPOJO> groupPermissions = groupPermissionsResp
									.getPermissions();

							for (PermissionPOJO pojo : groupPermissions) {
								int idx = permissions.indexOf(pojo);
								permissions.remove(idx);
								permissions.add(idx, pojo);
							}

						}

						showPermissions(permissions);
					}
				});

	}

	protected void showPermissions(List<PermissionPOJO> permissions) {
		divPermissions.removeClassName("hide");
		int j = 0;
		Collections.sort(permissions, new Comparator<PermissionPOJO>() {
			@Override
			public int compare(PermissionPOJO pojo1, PermissionPOJO pojo2) {
				return pojo1.getName().getModule()
						.compareTo(pojo2.getName().getModule());
			}
		});

		Module mod = null;
		int col = 0;
		int row1 = 0;
		int row2 = 0;
		int i = 0;
		for (PermissionPOJO permission : permissions) {

			if (mod == null) {
				i = row1;
				mod = permission.getName().getModule();
				HTMLPanel header = new HTMLPanel("" + mod.getDisplayName());
				header.addStyleName("permission-header");
				tblPermissions.setWidget(i, col + 1, header);
				tblPermissions.getFlexCellFormatter().setWidth(i, col, "50px");
				// tblPermissions.getFlexCellFormatter().addStyleName(i, col +
				// 1,
				// "permissions-td-header");

				++i;
			} else if (mod != permission.getName().getModule()) {
				if (col == 0) {
					col = 3;
					i = row2;
				} else {
					col = 0;
					i = row1;
				}
				mod = permission.getName().getModule();
				HTMLPanel header = new HTMLPanel("" + mod.getDisplayName());
				header.addStyleName("permission-header");
				tblPermissions.getFlexCellFormatter().setWidth(i, 0, "50px");
				tblPermissions.setWidget(i, col + 1, header);
				// tblPermissions.getFlexCellFormatter().addStyleName(i, col +
				// 1,
				// "permissions");

				++i;
			}

			tblPermissions.setWidget(i, col, new PermissionPanel(permission,
					type == TYPE.GROUP));
			HTMLPanel description = new HTMLPanel(permission.getDescription());
			description.addStyleName("item");
			tblPermissions.setWidget(i, col + 1, description);
			// tblPermissions.getFlexCellFormatter().addStyleName(i, col + 1,
			// "permissions");

			++i;

			if (col == 0) {
				row1 = i;
			} else {
				row2 = i;
			}
		}
	}

	public boolean isValid() {
		boolean isValid = true;
		switch (type) {
		case GROUP:
			isValid = isGroupValid();
			break;
		case USER:
			isValid = isUserValid();
			break;
		case ORG:
			if (isNullOrEmpty(txtUnitName.getValue())) {
				isValid = false;
				issues.addError("Unit name is mandatory");
				// txtUnitName.setValid(false);
			}
			break;
		}

		if (!isValid) {
			issues.removeStyleName("hide");
		} else {
			issues.addStyleName("hide");
		}

		return isValid;
	}

	protected void setContext(String value) {
		UploadContext context = new UploadContext();
		context.setAction(UPLOADACTION.UPLOADUSERIMAGE);
		context.setContext("userId", value + "");
		context.setAccept("png,jpeg,jpg,gif");
		uploader.setContext(context);
	}

	public UserGroup getGroup() {
		UserGroup group = this.group;
		if (group == null) {
			group = new UserGroup();
		}

		group.setFullName(txtDescription.getValue());
		group.setName(txtGroupname.getValue());

		List<PermissionPOJO> permissions = getPermissions();
		group.setPermissions(permissions);

		return group;
	}

	private List<PermissionPOJO> getPermissions() {
		int rowCount = tblPermissions.getRowCount();
		List<PermissionPOJO> permissions = new ArrayList<PermissionPOJO>();
		for (int i = 0; i < rowCount; i++) {

			// Col Set 1
			Widget w = tblPermissions.getWidget(i, 0);
			PermissionPanel panel = getPermissionPanel(w);
			if (panel != null) {
				PermissionPOJO permission = panel.getPermission();
				if (permission.isPermissionGranted()) {
					permissions.add(permission);
				}
			}

			// Col Set 2
			int cellCount = tblPermissions.getCellCount(i);
			if (cellCount > 2) {
				w = tblPermissions.getWidget(i, 3);
				panel = getPermissionPanel(w);
				if (panel != null) {
					PermissionPOJO permission = panel.getPermission();
					if (permission.isPermissionGranted()) {
						permissions.add(permission);
					}
				}
			}
		}
		return permissions;
	}

	private PermissionPanel getPermissionPanel(Widget w) {
		if (w instanceof PermissionPanel) {
			return ((PermissionPanel) w);
		}
		return null;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
		txtDescription.setValue(group.getFullName());
		txtGroupname.setValue(group.getName());
	}

	public HTUser getUser() {
		HTUser user = this.user;
		if (user == null) {
			user = new HTUser();
		}
		user.setEmail(txtEmail.getValue());
		user.setName(txtFirstname.getValue());
		user.setPassword(txtPassword.getValue());
		user.setSurname(txtLastname.getValue());
		user.setUserId(txtUserName.getValue());
		user.setGroups(lstGroups.getSelectedItems());
		user.setOrg(lstOrg.getValue());

		return user;
	}

	public Org getOrg() {
		Org org = this.org;
		if (org == null) {
			org = new Org();
		}
		org.setName(txtUnitName.getValue());
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
		txtUnitName.setValue(org.getName());
	}

	HTUser user;

	public void setUser(HTUser user) {
		this.user = user;
		txtEmail.setValue(user.getEmail());
		txtFirstname.setValue(user.getName());
		txtPassword.setValue(user.getPassword());
		txtConfirmPassword.setValue(user.getPassword());
		txtLastname.setValue(user.getSurname());
		txtUserName.setValue(user.getUserId());
		lstGroups.select(user.getGroups());
		setContext(user.getUserId());

	}

	private boolean isUserValid() {
		issues.clear();
		boolean valid = true;

		if (isNullOrEmpty(txtFirstname.getValue())) {
			valid = false;
			issues.addError("First Name is mandatory");
		}

		if (isNullOrEmpty(txtLastname.getValue())) {
			valid = false;
			issues.addError("First Name is mandatory");
		}

		if (isNullOrEmpty(txtEmail.getValue())) {
			valid = false;
			issues.addError("Email is mandatory");
		}

		if (isNullOrEmpty(txtPassword.getText())) {
			valid = false;
			issues.addError("Password is mandatory");
		} else {
			if (!txtPassword.getValue().equals(txtConfirmPassword.getValue())) {
				issues.addError("Password and confirm password fields do not match");
			}
		}

		return valid;
	}

	private boolean isGroupValid() {

		issues.clear();
		boolean valid = true;

		if (isNullOrEmpty(txtGroupname.getValue())) {
			valid = false;
			issues.addError("Group Name is mandatory");
		}

		return valid;
	}

	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	public HasClickHandlers getSaveUser() {
		return aSaveUser;
	}

	public HasClickHandlers getSaveGroup() {
		return aSaveGroup;
	}

	private void setType(TYPE type) {
		this.type = type;
		this.type = type;
		divUserDetails.addStyleName("hide");
		divGroupDetails.addStyleName("hide");
		divUnitDetails.addStyleName("hide");

		if (type == TYPE.GROUP) {
			divGroupDetails.removeStyleName("hide");
		} else if (type == TYPE.USER) {
			divUserDetails.removeStyleName("hide");
		} else {
			divUnitDetails.removeStyleName("hide");
		}
	}

	public void setGroups(List<UserGroup> groups) {
		lstGroups.addItems(groups);
	}
}
