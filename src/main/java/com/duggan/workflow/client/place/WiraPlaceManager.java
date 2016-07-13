package com.duggan.workflow.client.place;

import javax.inject.Inject;

import com.duggan.workflow.client.event.ShowMessageEvent;
import com.duggan.workflow.client.ui.AlertType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;
import com.wira.commons.client.security.CurrentUser;
import com.wira.commons.client.util.Definitions;

public class WiraPlaceManager extends PlaceManagerImpl {

	private final PlaceRequest defaultPlaceRequest;
	private final PlaceRequest errorPlaceRequest;
	private final PlaceRequest unauthorizedPlaceRequest;
	private CurrentUser currentUser;

	@Inject
	public WiraPlaceManager(EventBus eventBus,
			TokenFormatter tokenFormatter,
			@DefaultPlace String defaultPlaceNameToken,
			@ErrorPlace String errorPlaceNameToken,
			@UnauthorizedPlace String unauthorizedPlaceNameToken,
			Historian historian, CurrentUser currentUser) {
		super(eventBus, tokenFormatter, historian);
		this.currentUser = currentUser;

		defaultPlaceRequest = new PlaceRequest.Builder().nameToken(
				defaultPlaceNameToken).build();
		errorPlaceRequest = new PlaceRequest.Builder().nameToken(
				errorPlaceNameToken).build();
		unauthorizedPlaceRequest = new PlaceRequest.Builder().nameToken(
				unauthorizedPlaceNameToken).build();
	}

	@Override
	public void revealDefaultPlace() {
		revealPlace(defaultPlaceRequest, false);
	}

	@Override
	public void revealErrorPlace(String invalidHistoryToken) {
		revealPlace(errorPlaceRequest, false);
	}

	@Override
	public void revealUnauthorizedPlace(String unauthorizedHistoryToken) {
		String redirect = unauthorizedHistoryToken;
		 GWT.log("redirect unauthorizedHistoryToken = " +
				 redirect);
		

		/**
		 * Unauthorized Place is called whenever a user cannot access a presenter (Has no Permissions)
		 * or the user is not logged in.
		 * 
		 * At the same time, WiraPlaceManager appends the last URL(Place) as a redirect parameter
		 * for the LoginPresenter. This creates a scenario where if a user clicks on a place they
		 * have no permissions to view, the system navigates as follows: 
		 * 
		 * -> UnAuthorizedPresenter -> Login Presenter(already Logged in) -> Back to Unauthorized Presenter -> Login Presenter-> (EndLessLoop!)
		 * 
		 * The code below checks if user session already exists and breaks the cycle by removing the redirect parameter from the login placerequest
		 * 
		 */
		if(currentUser.isLoggedIn()){
			//Ignore redirect - This redirect was caused by an authorized(No permissions) access to a presenter
			revealPlace(unauthorizedPlaceRequest);
			getEventBus().fireEvent(new ShowMessageEvent(AlertType.WARNING, "You do not have sufficient rights!",true));
		}else{
			revealPlace(new PlaceRequest.Builder()
			.nameToken(NameTokens.splash).with(Definitions.REDIRECT, redirect).build());
		}
		
	}

}
