package com.duggan.workflow.client.ui.login;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.CurrentUser;
import com.duggan.workflow.shared.requests.LoginRequest;
import com.duggan.workflow.shared.responses.LoginRequestResult;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;

public class LoginPresenter extends
		Presenter<LoginPresenter.MyView, LoginPresenter.MyProxy> {

	public interface MyView extends View {

		String getUsername();
		String getPassword();
		HasClickHandlers getLoginBtn();
		void setPasswordKeyHandler(KeyDownHandler keyHandler);
		boolean isValid();
		void setError(String err);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.login)
	public interface MyProxy extends ProxyPlace<LoginPresenter> {
	}

	@Inject
	DispatchAsync requestHelper;
	
	@Inject PlaceManager placeManager;

	@Inject LoginGateKeeper gateKeeper;
	
	final CurrentUser user;
	
	@Inject
	public LoginPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final CurrentUser user) {
		super(eventBus, view, proxy);
		this.user = user;
	}

	@Override
	protected void revealInParent() {
		RevealRootLayoutContentEvent.fire(this, this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		if(AppContext.isValid()){
			placeManager.revealDefaultPlace();
			return;
		}
	}
	@Override
	protected void onBind() {
		super.onBind();

		getView().getLoginBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				login();
			}
		});

		KeyDownHandler keyHandler = new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					login();
				}
			}
		};
		
		getView().setPasswordKeyHandler(keyHandler);
	}

	protected void onReset() {
		Window.setTitle("Tasks");	
	};
	
	protected void login() {		
		if (getView().isValid()) {
			requestHelper.execute(new LoginRequest(getView().getUsername(),
					getView().getPassword()),
					new TaskServiceCallback<LoginRequestResult>() {
						@Override
						public void processResult(LoginRequestResult result) {
							boolean isValid = result.isValid();
							if(isValid){								
								AppContext.setSessionValues(
										result.getUser().getId(), result.getUser().getName(), result.getSessionId());
								
								//System.err.println(AppContext.getLastRequestUrl());
//								if(AppContext.getLastRequestUrl()!=null &&
//										AppContext.getLastRequestUrl()!=NameTokens.login){
//									History.newItem(AppContext.getLastRequestUrl());
//								}else{
									placeManager.revealDefaultPlace();
								
								
							}else{
								getView().setError("Wrong username or password");
							}
						}
					});
		}
	}

}
