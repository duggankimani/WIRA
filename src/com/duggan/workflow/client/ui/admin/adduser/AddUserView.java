package com.duggan.workflow.client.ui.admin.adduser;

import com.duggan.workflow.client.ui.admin.adduser.AddUserPresenter.TYPE;
import com.duggan.workflow.client.ui.admin.component.ListItem;
import com.duggan.workflow.client.ui.admin.component.ListItem.OnCloseHandler;
import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.PasswordField;
import com.duggan.workflow.client.ui.component.TextArea;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Listable;
import com.duggan.workflow.shared.model.UserGroup;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class AddUserView extends PopupViewImpl implements
		AddUserPresenter.MyView {

	private final Widget widget;
	@UiField HTMLPanel divUserDetails;
	@UiField HTMLPanel divGroupDetails;
	@UiField IssuesPanel issues;
	@UiField Anchor aClose;

	@UiField TextField txtUserName;
	@UiField TextField txtFirstname;
	@UiField TextField txtLastname;
	@UiField TextField txtEmail;
	@UiField PasswordField txtPassword;
	@UiField PasswordField txtConfirmPassword;
	
	@UiField TextField txtGroupname;
	@UiField TextArea txtDescription;
	@UiField TextField txtUsers;

	@UiField PopupPanel AddUserDialog;
	@UiField FocusPanel sltContainer;
	@UiField Element sltBox;
	@UiField Element sltDrop;
	@UiField Anchor liSelectItem;
	
	@UiField Anchor aSaveGroup;
	@UiField Anchor aSaveUser;
	
	@UiField BulletListPanel ulPanel;
	@UiField BulletListPanel ulSelectResults;
	
	TYPE type;
	
	public interface Binder extends UiBinder<Widget, AddUserView> {
	}

	@Inject
	public AddUserView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		for(int i=0 ; i<4; i++){
			UserGroup group = new UserGroup("User Group "+i);
			ListItem<UserGroup> itemWidget = new ListItem<UserGroup>(group);
			
			itemWidget.addOnCloseHandler(new OnCloseHandler() {
				@Override
				public void onItemClosed(ListItem source, Listable value) {
					System.out.println(value.getName());
					ulPanel.remove(source);
				}
				
			});
			ulPanel.add(itemWidget);
		}
		
		sltContainer.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				sltDrop.removeClassName("hidden");
				sltBox.addClassName("select2-dropdown-open");
			}
		});
		
		sltContainer.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				//sltDrop.addClassName("hidden");
			}
		});
		
		liSelectItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//ulSelectResults.
			}
		});
		

		
	
		//----Calculate the Size of Screen; To be Centralized later -----
		int height = Window.getClientHeight();
		int width = Window.getClientWidth();
		
		double height1=(10.0/100.0)*height;
		double width1= (40.0/100.0)*width;
		
		AddUserDialog.setPopupPosition((int)width1,(int)height1);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public boolean isValid(){
		
		boolean isValid=true;
		
		switch (type) {
		case GROUP:
			isValid =  isGroupValid();
			break;

		default:
			isValid =  isUserValid();
			break;
		}
		
		return isValid;
	}
	
	public UserGroup getGroup(){
		UserGroup group = new UserGroup();
		group.setFullName(txtDescription.getValue());
		group.setName(txtGroupname.getValue());
		
		return group;
	}
	
	public void setGroup(UserGroup group){
		txtDescription.setValue(group.getFullName());
		txtGroupname.setValue(group.getName());
	}
	
	public HTUser getUser(){
		HTUser user = new HTUser();
		user.setEmail(txtEmail.getValue());
		user.setName(txtFirstname.getValue());
		user.setPassword(txtPassword.getValue());
		user.setSurname(txtLastname.getValue());
		user.setUserId(txtUserName.getValue());
		//user.setGroups();
		
		return user;
	}
	
	public void setUser(HTUser user){
		txtEmail.setValue(user.getEmail());
		txtFirstname.setValue(user.getName());
		txtPassword.setValue(user.getPassword());
		txtConfirmPassword.setValue(user.getPassword());
		txtLastname.setValue(user.getSurname());
		txtUserName.setValue(user.getUserId());
	}

	private boolean isUserValid() {
		issues.clear();
		boolean valid=true;
		
		if(isNullOrEmpty(txtFirstname.getValue())){
			valid = false;
			issues.addError("First Name is mandatory");
		}
		
		if(isNullOrEmpty(txtLastname.getValue())){
			valid = false;
			issues.addError("First Name is mandatory");
		}
		
		if(isNullOrEmpty(txtEmail.getValue())){
			valid = false;
			issues.addError("Email is mandatory");
		}
		
		if(isNullOrEmpty(txtPassword.getText())){
			valid=false;
			issues.addError("Password is mandatory");
		}else{
			if(!txtPassword.getValue().equals(txtConfirmPassword.getValue())){
				issues.addError("Password and confirm password fields do not match");
			}
		}
		
		
		
		return valid;
	}

	private boolean isGroupValid() {
		
		issues.clear();
		boolean valid=true;
		
		if(isNullOrEmpty(txtGroupname.getValue())){
			valid = false;
			issues.addError("Group Name is mandatory");
		}
		
		return valid;
	}
	
	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	public HasClickHandlers getSaveUser(){
		return aSaveUser;
	}
	
	public HasClickHandlers getSaveGroup(){
		return aSaveGroup;
	}
	
	@Override
	public void setType(TYPE type) {
		this.type=type;
		if(type==TYPE.GROUP){
			divGroupDetails.removeStyleName("hide");
			divUserDetails.addStyleName("hide");
		}else{
			divUserDetails.removeStyleName("hide");
			divGroupDetails.addStyleName("hide");
		}
	}
}
