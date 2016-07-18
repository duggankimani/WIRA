package com.wira.login.client.activateaccount;

import java.util.logging.Logger;

import com.duggan.workflow.client.place.NameTokens;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.wira.commons.client.service.ServiceCallback;
import com.wira.commons.shared.models.CurrentUserDto;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.request.GetUserRequest;
import com.wira.commons.shared.response.GetUserRequestResult;
import com.wira.login.client.component.PasswordView;
import com.wira.login.shared.request.ActivateAccountRequest;
import com.wira.login.shared.request.ResetAccountRequest;
import com.wira.login.shared.response.ActivateAccountResponse;
import com.wira.login.shared.response.ResetAccountResponse;

public class ActivateAccountPresenter
		extends
		Presenter<ActivateAccountPresenter.IActivateAccountView, ActivateAccountPresenter.IActivateAccountProxy> {

	public interface IActivateAccountView extends View {

		void bindUser(HTUser user);

		boolean isValid();

		String getPassword();

		HasClickHandlers getSubmit();

		void setError(String string);

		void setLoginButtonEnabled(boolean b);

		void changeWidget(String reason);

		HasClickHandlers getResendButton();

		String getEmail();

		void showProcessing(boolean b);

		HasClickHandlers getSendActivationLink();

		void addError(String message);

		void showMessage(String errorMessage, String errorType);

		HasClickHandlers getProceedToLogin();

		HasKeyDownHandlers getPasswordTextField();

		HasKeyDownHandlers getEmailTextField();

		PasswordView getPanelPasswordWidget();

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.activateacc)
	@NoGatekeeper
	public interface IActivateAccountProxy extends
			ProxyPlace<ActivateAccountPresenter> {
	}

	@Inject
	DispatchAsync requestHelper;

	@Inject
	PlaceManager placeManager;

	// Loaded Page from Presenter
	private String reason;

	private HTUser user;

	private static final Logger LOGGER = Logger
			.getLogger(ActivateAccountPresenter.class.getName());

	@Inject
	public ActivateAccountPresenter(final EventBus eventBus,
			final IActivateAccountView view, final IActivateAccountProxy proxy,
			final CurrentUserDto currentUser) {
		super(eventBus, view, proxy);
	}

	private KeyDownHandler changePasswordKeyHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				doChangePassword();
			}
		}
	};

	@Override
	protected void onBind() {
		super.onBind();

		getView().getPasswordTextField().addKeyDownHandler(
				changePasswordKeyHandler);

		getView().getSubmit().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doChangePassword();
			}
		});

		getView().getResendButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!getView().getEmail().isEmpty()) {
					getView().showProcessing(true);
					getView().getPanelPasswordWidget().clearErrors();
					requestHelper.execute(new ResetAccountRequest(getView()
							.getEmail()),
							new ServiceCallback<ResetAccountResponse>() {
								@Override
								public void processResult(
										ResetAccountResponse aResponse) {
									HTUser dto = aResponse.getUser();
									if (dto == null) {
										getView().setError(
												"Account belonging to '"
														+ getView().getEmail()
														+ "' was not found");

									} else {
										getView()
												.getPanelPasswordWidget()
												.addMessage(
														"Your account reset instructions have been sent"
																+ " to your email - '"
																+ dto.getEmail()
																+ "'. <a href=\"login.html#/login\">Back to login</a>");
										getView().getPanelPasswordWidget()
												.showReset(false);
										// placeManager.revealPlace(new
										// PlaceRequest.Builder()
										// .nameToken(NameTokens.login).build());
									}
									getView().showProcessing(false);
								}

							});
				} else {
					getView().setError(
							" You must provide a registered email address");
				}
			}
		});
	}

	@Override
	protected void revealInParent() {
		RevealRootLayoutContentEvent.fire(this, this);
	}

	protected void doChangePassword() {
		if (getView().isValid()) {
			if (user != null) {
				user.setPassword(getView().getPassword());

				ActivateAccountRequest action = new ActivateAccountRequest(user);
				requestHelper.execute(action,
						new ServiceCallback<ActivateAccountResponse>() {
							@Override
							public void processResult(
									ActivateAccountResponse aResponse) {
								if (aResponse.getErrorCode() != 0
										&& aResponse.getErrorMessage() != null) {
									getView()
											.setError(
													"Password update failed due to an error");
								} else {
									Window.alert("Your password has been successfully updated");
									Window.Location.replace("login.html");
								}

							}
						});
			}
		}
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		getView().getPanelPasswordWidget().getIssues().clear();
		getView().getPanelPasswordWidget().getTxtEmail().setText("");
		String refId = request.getParameter("uid", null);
		String reason = request.getParameter("reason", null);
		this.reason = reason;

		if (reason != null) {
			getView().changeWidget(reason);
			if (!refId.equals("acc")) {
				GetUserRequest action = new GetUserRequest();
				action.setRefId(refId);
				requestHelper.execute(action,
						new ServiceCallback<GetUserRequestResult>() {
							@Override
							public void processResult(
									GetUserRequestResult aResponse) {
								HTUser user = aResponse.getUser();
								ActivateAccountPresenter.this.user = user;
								getView().bindUser(user);
							}
						});
			}
		}

	}

}