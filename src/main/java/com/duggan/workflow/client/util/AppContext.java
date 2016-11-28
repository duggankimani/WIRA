package com.duggan.workflow.client.util;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.shared.events.ContextLoadedEvent;
import com.duggan.workflow.shared.requests.GetContextRequest;
import com.duggan.workflow.shared.responses.GetContextRequestResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.wira.commons.client.CookieManager;
import com.wira.commons.client.security.CurrentUser;
import com.wira.commons.client.util.Definitions;
import com.wira.commons.shared.models.CurrentUserDto;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.REPORTVIEWIMPL;
import com.wira.commons.shared.models.Version;

/**
 * 
 * @author duggan
 *
 */
public class AppContext {

	@Inject
	static DispatchAsync dispatcher;
	@Inject
	static EventBus eventBus;
	@Inject
	static PlaceManager placeManager;
	static REPORTVIEWIMPL reportViewImpl;

	static String organizationName;

	@Inject
	static CurrentUser user;
	
	@Inject static Version version;

	public static void setSessionValue(String name, String value) {
		CookieManager.setSessionValue(name, value);
	}

	public static String getSessionValue(String name) {
		return CookieManager.getSessionValue(name);
	}

	public static boolean isShowWelcomeWiget() {
		String val = CookieManager.getSessionValue(
				Definitions.SHOWWELCOMEWIDGET, "true");
		Boolean show = Boolean.valueOf(val);

		return show;
	}

	public static boolean isLoggedIn() {
		boolean isValid = user.isLoggedIn();
		if (isValid) {
			return true;
		}
		return false;
	}

	protected static void setUserValues(HTUser htuser) {
		CookieManager.setSessionValue(Definitions.ISADMINSESSION,
				htuser.isAdmin() ? "Y" : "N");
	}

	public static DispatchAsync getDispatcher() {
		return dispatcher;
	}

	public static PlaceManager getPlaceManager() {
		return placeManager;
	}

	public static void destroy() {
		user.clear();
		CookieManager.clear();
	}

	public static String getUserId() {
		return user.getUser()==null? null: user.getUser().getUserId();
	}

	public static String getUserNames() {
		return user.getUser().getName();
	}

	public static String getUserGroups() {
		return user.getUser().getGroupsAsString();
	}

	public static EventBus getEventBus() {
		return eventBus;
	}

	public static String getLastRequestUrl() {
		return Cookies.getCookie(Definitions.PENDINGREQUESTURL);
	}

	public static void fireEvent(GwtEvent event) {
		eventBus.fireEvent(event);
	}

	public static HTUser getContextUser() {
		return user.getUser();
	}

	public static boolean isCurrentUserAdmin() {

		boolean isCurrentUserAdmin = user.getUser()==null? false : user.getUser().isAdmin();
		return isCurrentUserAdmin;
	}

	public static boolean isCurrentUser(String userId) {

		if (getContextUser() == null) {
			return false;
		}

		return getContextUser().getUserId().equals(userId);
	}

	public static String getOrganizationName() {
		return organizationName;
	}

	public static String getBaseUrl() {

		String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
		if (moduleUrl.endsWith("/")) {
			moduleUrl = moduleUrl.substring(0, moduleUrl.length() - 1);
		}

		return moduleUrl;
	}

	public static String getUserImageUrl() {
		if(user.getUser().getPictureUrl()!=null){
			return user.getUser().getPictureUrl(); 
		}
		
		String moduleUrl = getBaseUrl();
		String url = moduleUrl + "/getreport?ACTION=GetUser&userId="
				+ user.getUser().getUserId();
		
		return url;
	}

	public static String getUserImageUrl(double width) {
		return getUserImageUrl() + "&width=" + width;
	}

	public static String getUserImageUrl(double width, double height) {
		return getUserImageUrl() + "&width=" + width + "&height=" + height;
	}

	public static REPORTVIEWIMPL getReportViewImpl() {
		return reportViewImpl;
	}

	public static String getLoggedInCookie() {
		return Cookies.getCookie(Definitions.AUTHENTICATIONCOOKIE);
	}

	public static void clear() {
		user.fromCurrentUserDto(new CurrentUserDto(false, new HTUser()));
	}

	public static void reloadContext() {
		dispatcher.execute(new GetContextRequest(),
				new TaskServiceCallback<GetContextRequestResult>() {
					@Override
					public void processResult(GetContextRequestResult result) {
						organizationName = result.getOrganizationName();
						user.fromCurrentUserDto(result.getCurrentUserDto());
						version.from(result.getVersion());
						reportViewImpl = result.getReportViewImpl();

						ContextLoadedEvent event = new ContextLoadedEvent(
								user.getUser(), version);
						event.setOrganizationName(organizationName);
						eventBus.fireEvent(event);
					}

				});

	}

	public static void updateContext(Version version2,
			String organizationName2, REPORTVIEWIMPL reportView) {
		setOrganizationName(organizationName2);
		reportViewImpl = reportView;
		if(version2!=null){
			version.from(version2);
		}
	}

	public static void setOrganizationName(String organizationName2) {
		organizationName = organizationName2;
	}
}
