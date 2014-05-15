package com.duggan.workflow.client.ui.login;

import java.util.Arrays;
import java.util.List;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.LoadAlertsEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.GetSettingsRequest;
import com.duggan.workflow.shared.requests.LoginRequest;
import com.duggan.workflow.shared.responses.GetSettingsResponse;
import com.duggan.workflow.shared.responses.LoginRequestResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class LoginPresenter extends
		Presenter<LoginPresenter.ILoginView, LoginPresenter.MyProxy> {

	public interface ILoginView  extends View {
		String getUsername();
		String getPassword();
		Anchor getLoginBtn();
		TextBox getPasswordBox();
		boolean isValid();
		void setError(String err);
		void showLoginProgress();
		void clearLoginProgress();
		void clearViewItems(boolean status);
		TextBox getUserNameBox();
		void clearErrors();
		void setOrgName(String orgName);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.login)
	public interface MyProxy extends ProxyPlace<LoginPresenter> {
	}

	@Inject
	DispatchAsync requestHelper;
	
	@Inject PlaceManager placeManager;

	@Inject LoginGateKeeper gateKeeper;
		
	String redirect=null;
	
	@Inject
	public LoginPresenter(final EventBus eventBus, final ILoginView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealRootLayoutContentEvent.fire(this, this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		redirect = request.getParameter("redirect", null);
		
		if(AppContext.isValid()){
			
			if(AppContext.isCurrentUserAdmin()){
				History.newItem(NameTokens.processes);
			}else{
				placeManager.revealDefaultPlace();
			}
			
			return;
		}
		loadName();
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
		
		getView().getUserNameBox().addKeyDownHandler(keyHandler);
		getView().getPasswordBox().addKeyDownHandler(keyHandler);
	}

	protected void onReset() {
		Window.setTitle("WIRA");	
		getView().clearViewItems(true);
	};
	
	private void loadName() {		
		//not secured -- probably a bad idea
		requestHelper.execute(new GetSettingsRequest(Arrays.asList(SETTINGNAME.ORGNAME)), 
				new TaskServiceCallback<GetSettingsResponse>() {
			@Override
			public void processResult(GetSettingsResponse aResponse) {
				List<Setting> settings = aResponse.getSettings();
				if(settings!=null && !settings.isEmpty()){
					Setting setting = settings.get(0);
					Value value = setting.getValue();
					if(value.getValue()!=null){
						String orgName = value.getValue().toString();
						getView().setOrgName(orgName);
					}
				}
			}
		});
	}

	protected void login() {		
		if (getView().isValid()) {
			getView().clearErrors();
			getView().showLoginProgress();
			requestHelper.execute(new LoginRequest(getView().getUsername(),
					getView().getPassword()),
					new TaskServiceCallback<LoginRequestResult>() {
						@Override
						public void processResult(LoginRequestResult result) {
							boolean isValid = result.isValid();
							if(isValid){
								AppContext.setSessionValues(result.getUser(), result.getSessionId());
									//placeManager.revealDefaultPlace();
									
								
									if(redirect!=null){
										boolean isAdmin = result.getUser().isAdmin();
//										System.err.println("Redirect= "+redirect+
//												" :: IsUserAdmin = "+isAdmin+
//												" :: "+result.getUser().getGroupsAsString());
										
										if(isAdmin && redirect.equals("home")){
											History.newItem(NameTokens.processes);
										}else{
											History.newItem(redirect);
										}
										
									}else{
										//System.err.println("No Redirect");
										if(result.getUser().isAdmin()){
											placeManager.revealPlace(new PlaceRequest(NameTokens.processes));
										}else{
											placeManager.revealDefaultPlace();
										}
										
									}
									AppContext.reloadContext();
									fireEvent(new LoadAlertsEvent());
									
							}else{
								getView().clearLoginProgress();
								getView().getPasswordBox().setText("");
								getView().setError("Wrong username or password");
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							getView().clearLoginProgress();
							super.onFailure(caught);
							getView().setError("Could authenticate user. Please report this to your administrator");
						}
					});
		}
	}

}
