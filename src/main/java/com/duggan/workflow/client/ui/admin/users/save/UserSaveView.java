package com.duggan.workflow.client.ui.admin.users.save;

import java.util.List;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.admin.component.ListItem;
import com.duggan.workflow.client.ui.admin.users.save.UserSavePresenter.TYPE;
import com.duggan.workflow.client.ui.component.AutoCompleteField;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.PasswordField;
import com.duggan.workflow.client.ui.component.TextArea;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Organization;
import com.duggan.workflow.shared.model.UserGroup;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class UserSaveView extends PopupViewImpl implements UserSavePresenter.IUserSaveView {

	private final Widget widget;
	@UiField
	HTMLPanel divUserDetails;
	@UiField
	HTMLPanel divGroupDetails;
	@UiField
	HTMLPanel divOrgDetails;
	@UiField
	IssuesPanel issues;
	@UiField
	Anchor aClose;

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
	TextField txtOrgName;
	@UiField
	TextArea txtOrgDescription;

	@UiField
	PopupPanel AddUserDialog;
	@UiField
	Anchor aSaveGroup;
	@UiField
	Anchor aSaveUser;

	@UiField
	Anchor aSaveOrg;

	@UiField
	SpanElement header;

	@UiField
	DivElement divUserSave;
	@UiField
	Uploader uploader;
	// @UiField ListField<UserGroup> lstGroups;
	@UiField
	AutoCompleteField<UserGroup> lstGroups;
	@UiField
	DropDownList<Organization> lstOrgs;

	TYPE type;
	private HTUser user;

	public interface Binder extends UiBinder<Widget, UserSaveView> {
	}

	@Inject
	public UserSaveView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		// ----Calculate the Size of Screen; To be Centralized later -----
		int height = Window.getClientHeight();
		int width = Window.getClientWidth();

		/* Percentage to the Height and Width */
		double height1 = (5.0 / 100.0) * height;
		double width1 = (50.0 / 100.0) * width;

		AddUserDialog.setPopupPosition((int) width1, (int) height1);

		txtUserName.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				setContext(event.getValue());
			}
		});

	}

	protected void setContext(String value) {
		UploadContext context = new UploadContext();
		context.setAction(UPLOADACTION.UPLOADUSERIMAGE);
		context.setContext("userId", value + "");
		context.setAccept("png,jpeg,jpg,gif");
		uploader.setContext(context);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public boolean isValid() {

		boolean isValid = true;

		switch (type) {
		case GROUP:
			isValid = isGroupValid();
			break;
		case ORGANIZATION:
			isValid = isOrgValid();
			break;
		default:
			isValid = isUserValid();
			break;
		}

		return isValid;
	}

	public UserGroup getGroup() {
		UserGroup group = new UserGroup();
		group.setFullName(txtDescription.getValue());
		group.setName(txtGroupname.getValue());

		return group;
	}
	
	public Organization getOrg() {
		Organization org = new Organization();
		org.setFullName(txtOrgDescription.getValue());
		org.setName(txtOrgName.getValue());

		return org;
	}

	public void setGroup(UserGroup group) {
		txtDescription.setValue(group.getFullName());
		txtGroupname.setValue(group.getName());
	}

	public void setOrganization(Organization org) {
		txtOrgDescription.setValue(org.getFullName());
		txtOrgName.setValue(org.getName());
	}

	public HTUser getUser() {
		HTUser user = new HTUser();
		user.setEmail(txtEmail.getValue());
		user.setName(txtFirstname.getValue());
		user.setPassword(txtPassword.getValue());
		user.setSurname(txtLastname.getValue());
		user.setUserId(txtUserName.getValue());
		user.setGroups(lstGroups.getSelectedItems());
		user.setOrganization(lstOrgs.getValue());

		return user;
	}

	public void setUser(HTUser user) {
		this.user = user;
		txtEmail.setValue(user.getEmail());
		txtFirstname.setValue(user.getName());
		txtPassword.setValue(user.getPassword());
		txtConfirmPassword.setValue(user.getPassword());
		txtLastname.setValue(user.getSurname());
		txtUserName.setValue(user.getUserId());
		lstGroups.select(user.getGroups());
		lstOrgs.setValue(user.getOrganization());
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

	private boolean isOrgValid() {

		issues.clear();
		boolean valid = true;

		if (isNullOrEmpty(txtOrgName.getValue())) {
			valid = false;
			issues.addError("Organization Name is mandatory");
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

	public HasClickHandlers getSaveOrg() {
		return aSaveOrg;
	}

	@Override
	public void setType(TYPE type) {
		this.type = type;
		if (type == TYPE.GROUP) {
			divGroupDetails.removeStyleName("hide");
			divUserDetails.addStyleName("hide");
			divUserSave.addClassName("hide");
			header.setInnerText("New Group");
		} else if (type == TYPE.USER) {
			divUserDetails.removeStyleName("hide");
			divGroupDetails.addStyleName("hide");
			divUserSave.removeClassName("hide");
			header.setInnerText("New User");
		} else {
			divOrgDetails.removeStyleName("hide");
			divGroupDetails.addStyleName("hide");
			divUserDetails.addStyleName("hide");
			divUserSave.addClassName("hide");
			header.setInnerText("New Organization");
		}
	}

	@Override
	public void setGroups(List<UserGroup> groups) {
		lstGroups.addItems(groups);
	}
	
	public void setOrgs(List<Organization> organizations) {
		lstOrgs.setItems(organizations);
		if(user!=null && user.getOrganization()!=null){
			lstOrgs.setValue(user.getOrganization());
		}
	}
}
