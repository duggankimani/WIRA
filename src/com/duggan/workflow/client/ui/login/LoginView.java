package com.duggan.workflow.client.ui.login;

import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class LoginView extends ViewImpl implements LoginPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, LoginView> {
	}

	@UiField Anchor aLogin;
	@UiField IssuesPanel issues;
	@UiField TextBox username;
	@UiField TextBox password;
	@UiField SpanElement loading;
	@UiField HTMLPanel loadingbox;

	
	@Inject
	public LoginView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		username.getElement().setAttribute("Placeholder", "Username");
		username.getElement().setId("userid");
		username.removeStyleName("gwt-TextBox");
		
		issues.addStyleName("alert alert-danger");
		issues.addStyleName("hide");
	
		
		password.getElement().setAttribute("Placeholder", "Password");
		password.getElement().setId("userid");
		password.removeStyleName("gwt-TextBox");
		
	}
	
	public String getUsername(){
		return username.getValue();
	};
	

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public String getPassword(){
		return password.getValue();	
	};
	
	public Anchor getLoginBtn(){
		return aLogin;
	}

	@Override
	public boolean isValid() {
		String username=getUsername();
		String pass=getPassword();
		boolean isValid=true;
		issues.clear();
		if(isNullOrEmpty(username)){
			issues.addError("Username required");
			issues.removeStyleName("hide");
			isValid=false;
		}
		if(isNullOrEmpty(pass)){
			issues.addError("Password required");
			issues.removeStyleName("hide");
			isValid=false;
		}
		return isValid;
	}
	
	boolean isNullOrEmpty(String value){
		return value==null || value.trim().length()==0;
	}


	@Override
	public void setError(String error) {
		issues.clear();
		issues.addError(error);
		issues.removeStyleName("hide");
	}
	
	public TextBox getUserNameBox(){
		return username;
	}
	
	public TextBox getPasswordBox(){
		return password;
	}

	@Override
	public void clearErrors() {
		issues.clear();
	}

	@Override
	public void showLoginProgress() {
		issues.addStyleName("hide");
		loadingbox.addStyleName("loading");
		loading.removeClassName("hide");
	}
	
	@Override
	public void clearLoginProgress() {
		issues.removeStyleName("hide");
		loadingbox.removeStyleName("loading");
		loading.addClassName("hide");
	}

	@Override
	public void clearViewItems(boolean status) {
		//remove loading
		loadingbox.removeStyleName("loading");
		loading.addClassName("hide");
		issues.addStyleName("hide");
		
		//remove any Data written
		username.setText("");
		password.setText("");		
	}
}
