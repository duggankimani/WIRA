package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.VersionManager;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.model.settings.Setting;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.CurrentUserDto;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.PermissionPOJO;
import com.wira.commons.shared.models.REPORTVIEWIMPL;
import com.wira.commons.shared.models.UserGroup;
import com.wira.commons.shared.response.BaseResponse;
import com.wira.login.shared.model.ActionType;
import com.wira.login.shared.request.LoginRequest;
import com.wira.login.shared.response.LoginRequestResult;

public class LoginRequestActionHandler extends
		AbstractActionHandler<LoginRequest, LoginRequestResult> {

	private final Provider<HttpServletRequest> httpRequest;

	Logger logger = Logger.getLogger(LoginRequestActionHandler.class);
	@Inject
	Provider<HttpServletRequest> request;

	@Inject
	Provider<HttpServletResponse> response;


	@Inject
	public LoginRequestActionHandler(
			final Provider<HttpServletRequest> httpRequest) {
		this.httpRequest = httpRequest;
	}

	@Override
	public void execute(LoginRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		LoginRequestResult result = (LoginRequestResult) actionResult;
		execLogin(action, result);
		
		if(result.getCurrentUserDto().isLoggedIn()){
			//Set Context Info
			result.setVersion(VersionManager.getVersion());
			
			//Permissions
			HTUser user = result.getCurrentUserDto().getUser();
			user.setPermissions((ArrayList<PermissionPOJO>) DB.getPermissionDao().getPermissionsForUser(user.getUserId()));
			result.getCurrentUserDto().setUser(user);
			
			Setting setting = SettingsDaoHelper.getSetting(SETTINGNAME.ORGNAME);
			if(setting!=null){
				Object value = setting.getValue().getValue();
				result.setOrganizationName(value==null? null: value.toString());
			}
					
			Setting reportView = SettingsDaoHelper.getSetting(SETTINGNAME.REPORT_VIEW_IMPL);
			if(reportView!=null && reportView.getValue()!=null && reportView.getValue().getValue()!=null){
				result.setReportViewImpl(REPORTVIEWIMPL.valueOf(reportView.getValue().getValue().toString()));
			}
		}
	}

	public void execLogin(LoginRequest action, LoginRequestResult result) {
		HTUser userDto;
		boolean isLoggedIn = true;

		if (action.getActionType() == ActionType.VIA_COOKIE) {
			logger.info("ActionType VIA_COOKIE LogInHandlerexecut(): loggedInCookie=" + action.getLoggedInCookie());
			userDto = getUserFromCookie(action.getLoggedInCookie());
		} else {
			userDto = getUserFromCredentials(action.getUsername(),
					action.getPassword());
		}

		String contextPath = getContextPath();
		logger.info("#Exec Login: Setting context path = " + contextPath);

		isLoggedIn = userDto != null;

		String loggedInCookie = "";
		if (isLoggedIn) {
			HttpSession session = request.get().getSession(true);
			loggedInCookie = createSessionCookie(action.getLoggedInCookie(),
					userDto);
			session.setAttribute(ServerConstants.AUTHENTICATIONCOOKIE,
					loggedInCookie);
			Cookie xsrfCookie = new Cookie(ServerConstants.AUTHENTICATIONCOOKIE,
					loggedInCookie);
			xsrfCookie.setHttpOnly(false);// http only
			xsrfCookie.setPath(contextPath);
			xsrfCookie.setMaxAge(3600 * 24 * 30); // Time in seconds (30 days)
			xsrfCookie.setSecure(false); // http(s) cookie
			response.get().addCookie(xsrfCookie);
		} else {
			// reset headers
			resetSession();
		}


		CurrentUserDto currentUserDto = new CurrentUserDto(isLoggedIn, userDto);

		logger.info("LogInHandlerexecut(): actiontype="
				+ action.getActionType());
		logger.info("LogInHandlerexecut(): currentUserDto=" + currentUserDto);
		logger.info("LogInHandlerexecut(): loggedInCookie=" + loggedInCookie);

		assert action.getActionType() == null;

		result.setValues(action.getActionType(), currentUserDto, loggedInCookie);
	}

	private String getContextPath() {
		String contextPath = request.get().getServletContext().getContextPath();

		if (!contextPath.isEmpty() && !contextPath.equals("/")) {
			contextPath = (contextPath.startsWith("/") ? "" : "/") + contextPath
					+ "/";
		} else {
			contextPath = "/";
		}
		return contextPath;
	}
	
	public void resetSession() {

		HttpSession session = request.get().getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.get().setHeader("Set-Cookie",
				ServerConstants.AUTHENTICATIONCOOKIE + "=deleted; path="
						+ getContextPath() + "; "
						+ "expires=Thu, 01 Jan 1970 00:00:00 GMT");
	}


	private String createSessionCookie(String loggedInCookie, HTUser user) {

		HttpSession session = httpRequest.get().getSession(true);
		Object sessionId = session.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
		if(sessionId == null){
			sessionId = UUID.randomUUID().toString();
		} 
		session.setAttribute(ServerConstants.AUTHENTICATIONCOOKIE, sessionId);
		session.setAttribute(ServerConstants.USER, user);
		
		return sessionId.toString();
	}

	private HTUser getUserFromCookie(String loggedInCookie) {

		HTUser user = null;
		HttpSession session = httpRequest.get().getSession(false);
		if(session==null){
			logger.info("getUserFromCookie(cookie="+loggedInCookie+") Session is null!!"); 
			return null;
		}
		
		Object sessionId = session
				.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
		Object sessionUser = session.getAttribute(ServerConstants.USER);

		boolean isValid = (session != null && sessionUser != null && 
				sessionId != null && sessionId.equals(loggedInCookie));

		if (isValid) {
			user = (HTUser) sessionUser;
			user.setGroups((ArrayList<UserGroup>) LoginHelper.get().getGroupsForUser(user.getUserId()));
		}
		
		logger.info("getUserFromCookie(cookie="+loggedInCookie+") Server sessionId= "+sessionId+", validity = "+isValid+", User = "+user); 

		return user;
	}

	private HTUser getUserFromCredentials(String username, String password) {
		HTUser user = null;
		boolean loggedIn = LoginHelper.get().login(username, password);
		if (loggedIn) {
			user = LoginHelper.get().getUser(username, true);
		}

		return user;
	}

	@Override
	public void undo(LoginRequest action, LoginRequestResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<LoginRequest> getActionType() {
		return LoginRequest.class;
	}
}
