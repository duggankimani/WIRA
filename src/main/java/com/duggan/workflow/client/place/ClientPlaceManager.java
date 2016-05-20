package com.duggan.workflow.client.place;

import javax.inject.Inject;

import com.duggan.workflow.client.security.CurrentUser;
import com.duggan.workflow.client.util.Definitions;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;

public class ClientPlaceManager extends PlaceManagerImpl {

	private final PlaceRequest defaultPlaceRequest;
	private final PlaceRequest errorPlaceRequest;
	private final PlaceRequest unauthorizedPlaceRequest;
	private CurrentUser currentUser;

	@Inject
	public ClientPlaceManager(EventBus eventBus,
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
		
		revealPlace(new PlaceRequest.Builder()
		.nameToken(NameTokens.login).with(Definitions.REDIRECT, redirect).build(), false);
	}

}
