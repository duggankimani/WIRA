package com.duggan.workflow.client.util;

import java.util.ArrayList;
import java.util.Date;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.model.Version;
import com.duggan.workflow.shared.requests.GetContextRequest;
import com.duggan.workflow.shared.responses.GetContextRequestResult;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;


/**
 * 
 * @author duggan
 *
 */
public class AppContext {
	
	@Inject static DispatchAsync dispatcher;
	@Inject static EventBus eventBus;
	@Inject static PlaceManager placeManager;
	static Version version;

	static String organizationName;
	
	private static final HTUser user = new HTUser();
	
	static Timer timer = new Timer() {
		
		@Override
		public void run() {
			reloadContext();
		}
	};

	public static void setSessionValues(HTUser htuser, String authCookie){
		setUserValues(htuser);
		CookieManager.setCookies(authCookie, new Date().getTime());
		
	}
	
	public static void setSessionValue(String name, String value){
		CookieManager.setSessionValue(name, value);
	}
	
	public static String getSessionValue(String name){
		return CookieManager.getSessionValue(name);
	}
	
	public static boolean isShowWelcomeWiget(){
		String val = CookieManager.getSessionValue(Definitions.SHOWWELCOMEWIDGET, "true");
		Boolean show = Boolean.valueOf(val);
		
		return show;
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
				if(req.getNameToken()!=null){
					String token = placeManager.buildHistoryToken(req);
					Cookies.setCookie(Definitions.PENDINGREQUESTURL, token);
				}
			}
		}
		return valid;
	}
	
	private static void checkNeedReloadState() {
		if(user.getUserId()==null){
			reloadContext();
		}
	}

	static boolean reloading =false;//Controlled reload
	public static void reloadContext() {
		if(!reloading){
			reloading=true;
			dispatcher.execute(new GetContextRequest(), new TaskServiceCallback<GetContextRequestResult>() {
				@Override
				public void processResult(GetContextRequestResult result) {
					System.err.println("Reloading Context!!!!!!!!!!!!!!!!!!!!");
					organizationName= result.getOrganizationName();
					setUserValues(result.getUser());
					version = result.getVersion();
					
					ContextLoadedEvent event = new ContextLoadedEvent(result.getUser(), version);
					event.setOrganizationName(organizationName);
					eventBus.fireEvent(event);
					reloading=false;
				}
				
				@Override
				public void onFailure(Throwable caught) {
					reloading=false;
					super.onFailure(caught);
				}
			});
		}
	}
	
	protected static void setUserValues(HTUser htuser) {
		user.setName(htuser.getName());
		user.setUserId(htuser.getUserId());
		user.setGroups(htuser.getGroups());
		user.setEmail(htuser.getEmail());
		user.setSurname(htuser.getSurname());
		user.setId(htuser.getId());
		CookieManager.setSessionValue(Definitions.ISADMINSESSION, htuser.isAdmin()? "Y":"N");
	}


	public static DispatchAsync getDispatcher(){
		return dispatcher;
	}
	
	public static PlaceManager getPlaceManager(){
		return placeManager;
	}

	public static void destroy(){
		setSessionValues(new HTUser(), null);
		CookieManager.clear();		
	}
	
	public static String getUserId(){
		return user.getUserId();
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

	public static void fireEvent(GwtEvent event) {
		eventBus.fireEvent(event);
	}
	
	public static HTUser getContextUser(){
		return user;
	}

	public static boolean isCurrentUserAdmin() {
		boolean isAdmin = CookieManager.isCurrentUserAdmin();
		if(!isAdmin){
			System.err.println("2) IsCurrentUserAdmin? NOOO :: on Refresh -- USER IS NOT AN ADMIN");
		}
		
		return isAdmin;
	}


	public static boolean isCurrentUser(String userId) {
		
		if(getContextUser()==null){	
			return false;
		}
		
		return getContextUser().getUserId().equals(userId);
	}

	public static String getOrganizationName() {
		return organizationName;
	}
	
	public static String getBaseUrl(){
		
		String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
		if (moduleUrl.endsWith("/")) {
			moduleUrl = moduleUrl.substring(0, moduleUrl.length() - 1);
		}
		
		return moduleUrl;
	}
	
	public static String getUserImageUrl(){
		String moduleUrl = getBaseUrl();				
		String url = moduleUrl
				+ "/getreport?ACTION=GetUser&userId="
				+ user.getUserId();
		return url;
	}
	
	public static String getUserImageUrl(double width){
		return getUserImageUrl()+"&width="+width;
	}
	
	public static String getUserImageUrl(double width, double height){
		return getUserImageUrl()+"&width="+width+"&height="+height;
	}

	public static void clear() {
		user.setEmail(null);
		user.setGroups(new ArrayList<UserGroup>());
		user.setId(null);
		user.setSurname(null);
		user.setPassword(null);
		user.setUserId(null);
	}
}
