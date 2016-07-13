package com.wira.login.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.wira.commons.shared.models.HTUser;

public class PasswordView extends Composite {

	private static PasswordWidgetUiBinder uiBinder = GWT
			.create(PasswordWidgetUiBinder.class);

	interface PasswordWidgetUiBinder extends UiBinder<Widget, PasswordView> {
	}

	@UiField
	Anchor aCancel;

	@UiField
	Anchor aSave;

	@UiField
	Anchor aResendAct;

	@UiField
	Anchor aSendActivation;

	@UiField
	Anchor aProceedToLogin;

	@UiField
	Element divActionButtons;
	@UiField
	Element divInstructionItems;

	@UiField
	MsgPanel issues;

	@UiField
	TextBox txtEmail;
	@UiField
	PasswordTextBox txtPassword;
	@UiField
	PasswordTextBox txtConfirmPassword;

	@UiField
	Element divPassword;
	@UiField
	Element divConfirmPassword;

	@UiField
	SpanElement spnInfo;

	@UiField
	HTMLPanel panelContainer;
	
	@UiField
	Element divInstructions;

	private boolean doValidation;

	public PasswordView() {
		initWidget(uiBinder.createAndBindUi(this));
		txtEmail.setValue("");
		txtEmail.getElement().setAttribute("disabled", "true");
		txtEmail.getElement().setAttribute("placeholder", "Key in your e-Mail Address");
		
		txtPassword.getElement().setAttribute("placeholder","Your New Password");
		txtConfirmPassword.getElement().setAttribute("placeholder","Confirm Password");
	}

	public HasClickHandlers getSaveButton() {
		return aSave;
	}

	public HasClickHandlers getResendButton() {
		return aResendAct;
	}

	public HasClickHandlers getCancelButton() {
		return aCancel;
	}

	public void setUser(HTUser user) {
		if(user==null){
			return;
		}
		txtEmail.setText(user.getEmail());
	}

	public boolean isValid() {
		boolean isValid = true;
		issues.clear();

		if (!doValidation) {
			return true;
		}

		if (isNullOrEmpty(txtPassword.getValue())) {
			issues.addError("Password is required");
			isValid = false;
		}

		if (isNullOrEmpty(txtConfirmPassword.getValue())) {
			issues.addError("'Confirm Password' is required");
			isValid = false;
		}

		if (!txtPassword.getValue().equals(txtConfirmPassword.getValue())) {
			issues.addError("Password and 'Confirm Password' fields do not match");
			isValid = false;
		}

		return isValid;
	}

	public String getPassword() {

		return txtPassword.getValue();
	}

	public void addError(String error) {
		issues.addError(error);
	}

	public void setCreatePassword(boolean isCreate) {
		if (isCreate) {
			panelContainer.removeStyleName("panel panel-default");
		} else {
			panelContainer.addStyleName("panel panel-default");
		}
	}

	public HasClickHandlers getSendActivationLink() {
		return aSendActivation;
	}

	public void showProcessing(boolean isProcessing) {
		if (isProcessing) {
			aResendAct.getElement().setAttribute("disabled", "disabled");
			aResendAct.setText("Processing");
			aSendActivation.getElement().setAttribute("disabled", "disabled");
			aSendActivation.setText("Processing");
		} else {
			aResendAct.getElement().removeAttribute("disabled");
			aResendAct.setText("Send Reset Email");
			aSendActivation.getElement().removeAttribute("disabled");
			aSendActivation.setText("Re-Send Activation Email");
		}
	}

	public void setSaveEnabled(boolean enable) {

	}

	public HasClickHandlers getChangePasswordButton() {
		return null;
	}

	public void changeWidget(String reason) {
		showSuccess(false);
		showReset(true);
		if (reason.equals("forgot")) {
			this.doValidation = false;
			spnInfo.setInnerText("Enter your registered email address and we'll send you the reset instructions.");
			txtEmail.getElement().removeAttribute("disabled");
			divConfirmPassword.addClassName("hide");
			divPassword.addClassName("hide");
			aResendAct.removeStyleName("hide");
			aSave.addStyleName("hide");
			aSendActivation.addStyleName("hide");
		} else if (reason.equals("activate")) {
			this.doValidation = false;
			spnInfo.setInnerText("Enter the email you used to do registration, and email will be sent with activation instructions.");
			txtEmail.getElement().removeAttribute("disabled");
			divConfirmPassword.addClassName("hide");
			divPassword.addClassName("hide");
			aResendAct.addStyleName("hide");
			aSave.addStyleName("hide");
			aSendActivation.removeStyleName("hide");
		} else {
			this.doValidation = true;
			spnInfo.setInnerText("You have requested to change your password. Please enter a new password below.");
			txtEmail.getElement().setAttribute("disabled", "disabled");
			divConfirmPassword.removeClassName("hide");
			divPassword.removeClassName("hide");
			aResendAct.addStyleName("hide");
			aSave.removeStyleName("hide");
			aSendActivation.addStyleName("hide");
		}
	}

	public String getEmail() {
		return txtEmail.getValue();
	}

	public HasKeyDownHandlers getEmailTextBox() {
		return txtEmail;
	}

	public HasKeyDownHandlers getPasswordTextBox() {
		return txtConfirmPassword;
	}

	public void showMessage(String errorMessage, String errorType) {
		if (errorType.equals("success")) {
			issues.addMessage(errorMessage);
//			panelMessage.removeStyleName("hide");
//			panelMessage.setStyleDependentName("alert-", true);
//			panelMessage.addStyleDependentName(errorType);
		} else {
			issues.clear();
			issues.removeStyleName("hide");
			issues.addError(errorMessage);
		}
	}

	public HasClickHandlers getProceedToLogin() {
		showSuccess(true);
		return aProceedToLogin;
	}

	private void showSuccess(boolean show) {
		if (show) {
			aProceedToLogin.setVisible(true);
			aResendAct.addStyleName("hide");
			aSave.addStyleName("hide");
			aSendActivation.addStyleName("hide");
			aCancel.addStyleName("hide");
			divInstructionItems.addClassName("hide");
			issues.addStyleName("hide");
//			panelMessage.removeStyleName("hide");
		} else {
			aProceedToLogin.setVisible(false);
			aResendAct.removeStyleName("hide");
			aSave.removeStyleName("hide");
			aSendActivation.removeStyleName("hide");
			aCancel.removeStyleName("hide");
			divInstructionItems.removeClassName("hide");
//			panelMessage.addStyleName("hide");
		}
	}

	public void clearErrors() {
		issues.clear();
	}

	public TextBox getTxtEmail() {
		return txtEmail;
	}

	public MsgPanel getIssues() {
		return issues;
	}

	public void addMessage(String message) {
		issues.addMessage(message);
	}

	public void showReset(boolean show) {
		if(show){
			divInstructions.removeClassName("hide");
		}else{
			divInstructions.addClassName("hide");
		}
	}
	
	public boolean isNullOrEmpty(String value){
		return value==null || value.trim().isEmpty();
	}
}
