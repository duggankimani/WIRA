package com.duggan.workflow.client.util;

import java.util.Date;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.requests.GetContextRequest;
import com.duggan.workflow.shared.responses.GetContextRequestResult;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;


/**
 * 
 * @author duggan
 *
 */
public class AppContext {
	
	@Inject static DispatchAsync dispatcher;
	@Inject static EventBus eventBus;
	@Inject static PlaceManager placeManager;

	static String organizationName;
	
	private static final HTUser user = new HTUser();
	
	static Timer timer = new Timer() {
		
		@Override
		public void run() {
			reloadContext();
		}
	};

	public static void setSessionValues(String userId, String fullName, String authCookie){
		user.setName(fullName);
		user.setId(userId);
		
		CookieManager.setCookies(authCookie, new Date().getTime());
		
	}
	
	 
	public static boolean isValid(){
		//System.err.println("Session Cookie Asked:: "+CookieManager.getAuthCookie());
		
		boolean valid = false;
		valid  = CookieManager.getAuthCookie()!=null;
		
		if(valid){
			checkNeedReloadState();
		}else{
			//store targetUrl
			PlaceRequest req = placeManager.getCurrentPlaceRequest();
			
			if(req!=null && !req.matchesNameToken(NameTokens.login)){
				String token = placeManager.buildHistoryToken(req);
				Cookies.setCookie(Definitions.PENDINGREQUESTURL, token);
			}
		}
		return valid;
	}
	
	private static void checkNeedReloadState() {
		if(user.getId()==null){
			reloadContext();
		}
	}

	private static void reloadContext() {
		dispatcher.execute(new GetContextRequest(), new TaskServiceCallback<GetContextRequestResult>() {
			@Override
			public void processResult(GetContextRequestResult result) {
				user.setId(result.getUser().getId());
				user.setName(result.getUser().getName());
				user.setGroups(result.getGroups());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);				
			}
		});
	}
	
	public static PlaceManager getPlaceManager(){
		return placeManager;
	}

	public static void destroy(){
		CookieManager.clear();
	}
	
	public static String getUserId(){
		return user.getId();
	}
	
	public static String getUserNames(){
		return user.getName();
	}
	
	public static String getUserGroups(){
		return user.getGroupsAsString();
	}
	
	public static EventBus getEventBus(){
		return eventBus;
	}
	
	public static String getLastRequestUrl(){
		return Cookies.getCookie(Definitions.PENDINGREQUESTURL);
	}
}
