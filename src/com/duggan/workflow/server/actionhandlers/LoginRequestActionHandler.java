package com.duggan.workflow.server.actionhandlers;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.requests.LoginRequest;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.LoginRequestResult;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class LoginRequestActionHandler extends
		BaseActionHandler<LoginRequest, LoginRequestResult> {

	private final Provider<HttpServletRequest> httpRequest;
	
	@Inject
	public LoginRequestActionHandler(final Provider<HttpServletRequest> httpRequest) {
		this.httpRequest=httpRequest;
	}

	@Override
	public void execute(LoginRequest action,
			BaseResult actionResult, ExecutionContext execContext)
			throws ActionException {
		String userId = action.getUsername();
		String password = action.getPassword();
		
		boolean loggedIn = LoginHelper.get().login(userId, password);
		
		LoginRequestResult result = (LoginRequestResult)actionResult;
		
		if(loggedIn){
			HttpSession session = httpRequest.get().getSession();			
			String sessionId = UUID.randomUUID().toString();
			session.setAttribute(ServerConstants.AUTHENTICATIONCOOKIE, sessionId);
			
			HTUser user = LoginHelper.get().getUser(userId);
			session.setAttribute(ServerConstants.USER, user);				
			result.setUser(user);
			result.setValid(true);
			result.setSessionId(sessionId);
		}		
		
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
