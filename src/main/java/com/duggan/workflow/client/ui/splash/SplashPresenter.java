package com.duggan.workflow.client.ui.splash;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.requests.GetContextRequest;
import com.duggan.workflow.shared.responses.GetContextRequestResult;
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
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.wira.commons.client.security.CurrentUser;
import com.wira.commons.shared.models.CurrentUserDto;
public class SplashPresenter extends Presenter<SplashPresenter.MyView, SplashPresenter.MyProxy>  {
    interface MyView extends View  {
    }

    @NoGatekeeper
    @NameToken(NameTokens.splash)
    @ProxyCodeSplit
    interface MyProxy extends ProxyPlace<SplashPresenter> {
    }

    @Inject DispatchAsync requestHelper;
    @Inject PlaceManager placeManager;
	private CurrentUser currentUser; 
    
    @Inject
    SplashPresenter(
            EventBus eventBus,
            MyView view, 
            MyProxy proxy, CurrentUser currentUser) {
        super(eventBus, view, proxy, RevealType.RootLayout);
		this.currentUser = currentUser;
        
    }
    
    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        loadContext();
    }

	private void loadContext() {
		
		requestHelper.execute(new GetContextRequest(), new TaskServiceCallback<GetContextRequestResult>() {
			@Override
			public void processResult(GetContextRequestResult aResponse) {
				if(!aResponse.getIsValid()){
					Window.Location.replace(NameTokens.loginPage);
					return;
				}
				setContextValues(aResponse);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.Location.replace(NameTokens.loginPage);
			}
		});
	}

	protected void setContextValues(GetContextRequestResult aResponse) {
		
		CurrentUserDto currentUserDto = aResponse.getCurrentUserDto();
		currentUser.fromCurrentUserDto(currentUserDto);
		
		PlaceRequest placeRequest = new PlaceRequest.Builder().
				nameToken(NameTokens.getOnLoginDefaultPage()).build();
		placeManager.revealPlace(placeRequest);
	}
    
    
}