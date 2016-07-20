package com.wira.login.client.signin;

import javax.inject.Inject;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class LoginView extends ViewImpl implements LoginPresenter.ILoginView {
	
	interface Binder extends UiBinder<Widget, LoginView> {
	}

	Anchor aLogin;
	Element ulRoot;
	Element issues;

	TextBox username;
	PasswordTextBox password;
	Element loading;
	HTMLPanel loadingbox;

	@Inject
	LoginView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));

		DOM.getElementById("mainContent").addClassName("hide");
		
		this.ulRoot = DOM.createElement("ul");

		// Login Button
		aLogin = new Anchor("Login");
		aLogin.addStyleName("btn-glow btn-block primary login");
		Element divLogin = DOM.getElementById("divLogin");
		HTMLPanel panel = HTMLPanel.wrap(divLogin);
		divLogin.removeAllChildren();
		panel.add(aLogin);
		
		username = TextBox.wrap(DOM.getElementById("username"));
		password = PasswordTextBox.wrap(DOM.getElementById("password"));
		loading = DOM.getElementById("loading");
		
		loadingbox = HTMLPanel.wrap(DOM.getElementById("loadingbox"));

		// Issues
		issues = DOM.getElementById("issues");
		issues.appendChild(ulRoot);
		username.getElement().setAttribute("Placeholder", "Username");
		username.removeStyleName("gwt-TextBox");

		issues.addClassName("alert alert-danger hide");

		password.getElement().setAttribute("Placeholder", "Password");
		password.removeStyleName("gwt-TextBox");

	}

	public HasClickHandlers getLogin() {
		return aLogin;
	}

	public void addError(String error) {
		Element liElement = DOM.createElement("li");
		liElement.setInnerHTML(error);
		this.ulRoot.appendChild(liElement);
	}

	public void clearErrors() {
		this.ulRoot.setInnerHTML("");
		issues.addClassName("hide");
	}

	public String getUsername() {
		return username.getValue();
	};

	public String getPassword() {
		return password.getValue();
	};

	public Anchor getLoginBtn() {
		return aLogin;
	}

	@Override
	public boolean isValid() {
		String username = getUsername();
		String pass = getPassword();
		boolean isValid = true;
		clearErrors();
		if (isNullOrEmpty(username)) {
			addError("Username required");
			isValid = false;
		}
		if (isNullOrEmpty(pass)) {
			addError("Password required");
			isValid = false;
		}
		return isValid;
	}

	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	@Override
	public void setError(String error) {
		clearErrors();
		addError(error);
		issues.removeClassName("hide");
	}

	public TextBox getUserNameBox() {
		return username;
	}

	public TextBox getPasswordBox() {
		return password;
	}

	@Override
	public void showLoginProgress(boolean show) {
		if (show) {
			loadingbox.addStyleName("loading");
			loading.removeClassName("hide");
		} else {
			loadingbox.removeStyleName("loading");
			loading.addClassName("hide");
		}
	}

	@Override
	public void clearViewItems(boolean status) {
		// remove loading
		loadingbox.removeStyleName("loading");
		loading.addClassName("hide");
		issues.addClassName("hide");

		username.setText("");
		password.setText("");
	}

	public void setOrgName(String orgName) {
//		spnCompanyName.setInnerText(orgName);
	}

	@Override
	public HasClickHandlers getLoginButton() {
		return aLogin;
	}

	@Override
	public void setLoginButtonEnabled(boolean isEnabled) {

	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		DOM.getElementById("mainContent").removeClassName("hide");
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		Window.alert("Detach!");
		
	}
}