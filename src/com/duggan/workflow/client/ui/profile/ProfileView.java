package com.duggan.workflow.client.ui.profile;

import java.util.List;

import gwtupload.client.IUploader;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.PasswordField;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent.ContextLoadedHandler;
import com.duggan.workflow.client.ui.images.ImageResources;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.UserGroup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ProfileView extends ViewImpl implements ProfilePresenter.IProfileView{

	private final Widget widget;
	@UiField Uploader uploader;
	@UiField FocusPanel panelPicture;
	@UiField FocusPanel uploadPanel;
	@UiField HTMLPanel panelPassword;
	@UiField DivElement divPassword;
	@UiField DivElement divGroups;
	
	@UiField TextField txtLastname;
	@UiField TextField txtFirstname;
	@UiField TextField txtUserName;
	@UiField TextField txtEmail;
	@UiField PasswordField txtPrevPassword;
	@UiField PasswordField txtPassword;
	@UiField PasswordField txtConfirmPassword;
	@UiField DivElement divSaveUser;
	@UiField Button btnSave;
	@UiField Button btnCancel;
	
	@UiField Button btnPassword;
	@UiField Button btnSavePassword;
	@UiField Button btnCancelPasswordSave;
	
	@UiField Image imgUser;
	
	@UiField IssuesPanel issues;
		
	public interface Binder extends UiBinder<Widget, ProfileView> {
	}

	@Inject
	public ProfileView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		uploader.addStyleName("custom-file-input");
		
		panelPicture.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				uploader.removeStyleName("hide");
			}
		});
		
		imgUser.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				imgUser.setUrl("img/blueman.png");
			}
		});
		
		uploadPanel.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				uploader.addStyleName("hide");
			}
		});
	
		btnPassword.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divPassword.addClassName("hide");
				panelPassword.removeStyleName("hide");
			}
		});
		
		btnCancelPasswordSave.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divPassword.removeClassName("hide");
				panelPassword.addStyleName("hide");
				issues.clear();
			}
		});
		
		KeyPressHandler kphandler = new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(valueChanged()){
					UIObject.setVisible(divSaveUser, true);
					UIObject.setVisible(btnPassword.getElement(), false);
				}else{
					UIObject.setVisible(divSaveUser, false);
					UIObject.setVisible(btnPassword.getElement(), true);
				}
			}
		};
		
		ValueChangeHandler<String> vhandler = new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(valueChanged()){
					UIObject.setVisible(divSaveUser, true);
					UIObject.setVisible(btnPassword.getElement(), false);
				}else{
					UIObject.setVisible(divSaveUser, false);
					UIObject.setVisible(btnPassword.getElement(), true);
				}
			}
		};
		
		txtFirstname.addKeyPressHandler(kphandler);
		txtEmail.addKeyPressHandler(kphandler);
		txtLastname.addKeyPressHandler(kphandler);
		
		txtFirstname.addValueChangeHandler(vhandler);
		txtEmail.addValueChangeHandler(vhandler);
		txtLastname.addValueChangeHandler(vhandler);
		
			
		UIObject.setVisible(divSaveUser, false);
		
		uploader.addOnFinishUploaderHandler(new IUploader.OnFinishUploaderHandler() {
			
			@Override
			public void onFinish(IUploader uploaderRef) {
				String url = imgUser.getUrl();
				imgUser.setUrl(url+"&version="+Random.nextInt());
			}
		});
	}

	public HTUser getUser(){
		
		HTUser user = new HTUser();
		user.setEmail(txtEmail.getText());
		user.setName(txtFirstname.getText());
		user.setSurname(txtLastname.getText());
		return user;
	}
	

	public void setUser(HTUser user){
		issues.clear();
		txtEmail.setValue(user.getEmail());
		txtFirstname.setValue(user.getName());
		txtLastname.setValue(user.getSurname());
		txtUserName.setValue(user.getUserId());
		email = user.getEmail();
		firstName = user.getName();
		lastName = user.getSurname();
		
		setContext(user.getUserId());
		setImage(user);
		
		List<UserGroup> groups = user.getGroups();
		if(groups!=null){
			String html = "";
			for(UserGroup group: groups){
				html = html.concat("<span class=\"label\">"+group.getDisplayName()+"</span>");
			}
			
			divGroups.setInnerHTML(html);
		}
		
		UIObject.setVisible(divSaveUser, false);
		UIObject.setVisible(btnPassword.getElement(), true);
		divPassword.removeClassName("hide");
		panelPassword.addStyleName("hide");
	}
	
	String email=null;
	String firstName=null;
	String lastName=null;
	protected boolean valueChanged() {
		
		if(!firstName.equals(txtFirstname.getValue())){
			return true;
		}
		if(!lastName.equals(txtLastname.getValue())){
			return true;
		}
		
		if(!email.equals(txtEmail.getValue())){
			return true;
		}
		
		return false;
	}
	
	protected void setContext(String value) {
		UploadContext context = new UploadContext();
		context.setAction(UPLOADACTION.UPLOADUSERIMAGE);
		context.setContext("userId", value);
		context.setAccept("png,jpeg,jpg,gif");
		uploader.setContext(context);
	}
	
	
	@Override
	public boolean isValid() {
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
		
		
		return valid;
	}

	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasClickHandlers getSaveUser() {
		
		return btnSave;
	}
	
	public void setImage(HTUser user) {
		String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
		if (moduleUrl.endsWith("/")) {
			moduleUrl = moduleUrl.substring(0, moduleUrl.length() - 1);
		}
		String url = moduleUrl
				+ "/getreport?ACTION=GetUser&width=179.0&userId="
				+ user.getUserId();
		imgUser.setUrl(url);
	}

	@Override
	public HasClickHandlers getChangePassword() {

		return btnSavePassword;
	}

	@Override
	public boolean isPasswordChangeValid() {
		issues.clear();
		boolean valid=true;
		
		if(isNullOrEmpty(txtPrevPassword.getValue())){
			valid = false;
			issues.addError("Previous password is mandatory");
		}
		
		if(isNullOrEmpty(txtPassword.getValue())){
			valid = false;
			issues.addError("New Password is mandatory");
		}
		
		if(isNullOrEmpty(txtConfirmPassword.getValue())){
			valid = false;
			issues.addError("Confirm Password is mandatory");
		}
		
		
		if(valid && !(txtPassword.getText().equals(txtConfirmPassword.getText()))){
			valid = false;
			issues.addError("New Password and Confirm Password must be same");
		}
		
		return valid;
	}

	@Override
	public String getPassword() {
	
		return txtPassword.getText();
	}

	@Override
	public void setError(String error) {
		issues.clear();
		issues.addError(error);
	}

	@Override
	public String getPreviousPassword() {
		
		return txtPrevPassword.getText();
	}
	
	public HasClickHandlers getCancelSaveUser(){
		return btnCancel;
	}
}
