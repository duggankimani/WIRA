package com.duggan.workflow.server.actionhandlers;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.VersionManager;
import com.duggan.workflow.shared.model.ActionType;
import com.duggan.workflow.shared.model.CurrentUserDto;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.settings.REPORTVIEWIMPL;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.LoginRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.LoginRequestResult;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class LoginRequestActionHandler extends
		AbstractActionHandler<LoginRequest, LoginRequestResult> {

	private final Provider<HttpServletRequest> httpRequest;

	Logger logger = Logger.getLogger(LoginRequestActionHandler.class);

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

		isLoggedIn = userDto != null;

		String loggedInCookie = "";
		if (isLoggedIn) {
			loggedInCookie = createSessionCookie(action.getLoggedInCookie(),
					userDto);
		}

		CurrentUserDto currentUserDto = new CurrentUserDto(isLoggedIn, userDto);

		logger.info("LogInHandlerexecut(): actiontype="
				+ action.getActionType());
		logger.info("LogInHandlerexecut(): currentUserDto=" + currentUserDto);
		logger.info("LogInHandlerexecut(): loggedInCookie=" + loggedInCookie);

		assert action.getActionType() == null;

		result.setValues(action.getActionType(), currentUserDto, loggedInCookie);
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
			user.setGroups(LoginHelper.get().getGroupsForUser(user.getUserId()));
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
