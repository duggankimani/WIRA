package com.wira.login.client.activateaccount;

import javax.inject.Inject;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.commons.shared.models.HTUser;
import com.wira.login.client.component.PasswordView;

class ActivateAccountView extends ViewImpl implements ActivateAccountPresenter.IActivateAccountView {
    interface Binder extends UiBinder<Widget, ActivateAccountView> {
    }

	PasswordView panelPasswordWidget;
	
    @Inject
    ActivateAccountView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));

        panelPasswordWidget  = new PasswordView();
        
    	Element divAccountsReset = DOM.getElementById("divAccountsReset");
        divAccountsReset.removeAllChildren();	
        HTMLPanel panel = HTMLPanel.wrap(divAccountsReset);
        panel.add(panelPasswordWidget);
    }
    
    @Override
    protected void onAttach() {
    	super.onAttach();
    }

	@Override
	public void bindUser(HTUser user) {
		panelPasswordWidget.setUser(user);
	}

	@Override
	public boolean isValid() {
		return panelPasswordWidget.isValid();
	}

	@Override
	public String getPassword() {
		return panelPasswordWidget.getPassword();
	}

	@Override
	public HasClickHandlers getSubmit() {
		return panelPasswordWidget.getSaveButton();
	}

	@Override
	public void setError(String error) {
		panelPasswordWidget.clearErrors();
		panelPasswordWidget.addError(error);
	}

	@Override
	public void setLoginButtonEnabled(boolean enable) {
		panelPasswordWidget.setSaveEnabled(enable);
	}

	@Override
	public void changeWidget(String reason) {
		panelPasswordWidget.changeWidget(reason);
	}

	@Override
	public HasClickHandlers getResendButton() {
		return panelPasswordWidget.getResendButton();
	}

	public HasClickHandlers getSendActivationLink() {
		return panelPasswordWidget.getSendActivationLink();
	}

	@Override
	public String getEmail() {
		return panelPasswordWidget.getEmail();
	}

	@Override
	public void showProcessing(boolean showProcessing) {
		panelPasswordWidget.showProcessing(showProcessing);
	}

	@Override
	public void addError(String message) {
		panelPasswordWidget.addError(message);
	}

	@Override
	public void showMessage(String errorMessage, String errorType) {
		panelPasswordWidget.showMessage(errorMessage, errorType);
	}

	@Override
	public HasClickHandlers getProceedToLogin() {
		return panelPasswordWidget.getProceedToLogin();
	}

	public HasKeyDownHandlers getEmailTextField() {
		return panelPasswordWidget.getEmailTextBox();
	}

	public HasKeyDownHandlers getPasswordTextField() {
		return panelPasswordWidget.getPasswordTextBox();
	}

	public PasswordView getPanelPasswordWidget() {
		return panelPasswordWidget;
	}


    
}