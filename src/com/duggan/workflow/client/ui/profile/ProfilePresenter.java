package com.duggan.workflow.client.ui.profile;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.duggan.workflow.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent.ContextLoadedHandler;
import com.duggan.workflow.client.ui.events.LoadAlertsEvent;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.requests.CheckPasswordRequest;
import com.duggan.workflow.shared.requests.GetUserRequest;
import com.duggan.workflow.shared.requests.SaveUserRequest;
import com.duggan.workflow.shared.requests.UpdatePasswordRequest;
import com.duggan.workflow.shared.responses.CheckPasswordRequestResult;
import com.duggan.workflow.shared.responses.GetUserRequestResult;
import com.duggan.workflow.shared.responses.SaveUserResponse;
import com.duggan.workflow.shared.responses.UpdatePasswordResponse;

public class ProfilePresenter extends
		PresenterWidget<ProfilePresenter.IProfileView> implements ContextLoadedHandler {

	public interface IProfileView extends View {
		public boolean isValid();

		public HasClickHandlers getSaveUser();

		public HTUser getUser();

		public void setUser(HTUser user);

		public HasClickHandlers getChangePassword();

		public boolean isPasswordChangeValid();

		public String getPassword();

		public void setError(String error);

		public String getPreviousPassword();
		
		public HasClickHandlers getCancelSaveUser();
	}
	
	@Inject DispatchAsync requestHelper;

	HTUser user;
	
	@Inject
	public ProfilePresenter(final EventBus eventBus, final IProfileView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ContextLoadedEvent.TYPE, this);
		getView().getSaveUser().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					HTUser htuser = getView().getUser();
					assert user!=null;
					
					user.setEmail(htuser.getEmail());
					user.setName(htuser.getName());
					user.setSurname(htuser.getSurname());
					save();
				}
			}
		});
		
		getView().getCancelSaveUser().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getView().setUser(user);
			}
		});
	
		getView().getChangePassword().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isPasswordChangeValid()){
					assert user!=null;
					final String previousPassword = getView().getPreviousPassword();
					
					requestHelper.execute(new CheckPasswordRequest(user.getUserId(), previousPassword),
							new TaskServiceCallback<CheckPasswordRequestResult>() {
						@Override
						public void processResult(
								CheckPasswordRequestResult aResponse) {
							
							if(aResponse.getIsValid()){
								String password = getView().getPassword();//new password								
								user.setPassword(password);

								requestHelper.execute(new UpdatePasswordRequest(user.getUserId(), password),
										new TaskServiceCallback<UpdatePasswordResponse>() {
									public void processResult(UpdatePasswordResponse aResponse) {
										getView().setUser(user);
									};
								});
								
							}else{
								getView().setError("The password provided is incorrect");
							}
							
						}
					});
					
					
				}
			}
		});
	}
	
	protected void save() {
		SaveUserRequest request = new SaveUserRequest(user);
		requestHelper.execute(request, new TaskServiceCallback<SaveUserResponse>() {
			@Override
			public void processResult(SaveUserResponse result) {
				user = result.getUser();
				getView().setUser(user);
			}
		});
	}

	@Override
	protected void onReset() {
		super.onReset();
		fireEvent(new LoadAlertsEvent());
		loadUser();
	}

	private void loadUser() {
		if(AppContext.getUserId()==null){
			return;
		}
		requestHelper.execute(new GetUserRequest(AppContext.getUserId()), 
				new TaskServiceCallback<GetUserRequestResult>() {
			@Override
			public void processResult(GetUserRequestResult aResponse) {
				user = aResponse.getUser();				
				assert user.getUserId()!=null;
				assert !user.getGroups().isEmpty();
				
				getView().setUser(user);
			}
		});
	}

	@Override
	public void onContextLoaded(ContextLoadedEvent event) {
		if(user==null)
			loadUser();
	}
}