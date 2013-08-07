package com.duggan.workflow.client.ui.login;

import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LoginView extends ViewImpl implements LoginPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, LoginView> {
	}

	@UiField Anchor aLogin;
	@UiField IssuesPanel issues;
	/*@UiField TextBox username;
	@UiField TextBox password; */

	
	@Inject
	public LoginView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		/*
		//Username Enter
		username.addKeyDownHandler(new KeyDownHandler() {			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				 if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
			            Window.alert("Link this feature");
			         }				
			}
		});
		
		//Password Enter
		password.addKeyDownHandler(new KeyDownHandler() {			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				 if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
			            Window.alert("Link this feature");
			         }				
			}
		});
		*/
	}
	
	public native String getUsername()/*-{
		var userid = $doc.getElementById("userid").value;
		return userid;
	}-*/;

	@Override
	public Widget asWidget() {
		return widget;
		
	}
	
	public native String getPassword()/*-{
		var userpass = $doc.getElementById("userpass").value;
		return userpass;	
	}-*/;
	
	public HasClickHandlers getLoginBtn(){
		return aLogin;
	}

	@Override
	public void setPasswordKeyHandler(KeyDownHandler keyHandler) {
		//passwordBox.addKeyDownHandler(keyHandler);
	}

	@Override
	public boolean isValid() {
		String username=getUsername();
		String pass=getPassword();
		boolean isValid=true;
		
		if(isNullOrEmpty(username)){
			issues.addError("Username required");
			isValid=false;
		}
		
		if(isNullOrEmpty(pass)){
			issues.addError("Password required");
			isValid=false;
		}
		
		return isValid;
	}
	
	boolean isNullOrEmpty(String value){
		return value==null || value.trim().length()==0;
	}


	@Override
	public void setError(String error) {
		issues.addError(error);
	}
}
