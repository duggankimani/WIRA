package com.duggan.workflow.server.actionhandlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;
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
		//new UserDaoHelper().login(action,result);
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
