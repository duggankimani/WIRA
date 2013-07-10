package com.duggan.workflow.server.actionhandlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.duggan.workflow.shared.requests.LogoutAction;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.LogoutActionResult;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class LogoutActionHandler extends
		BaseActionHandler<LogoutAction, LogoutActionResult> {

	private final Provider<HttpServletRequest> httpRequest;
	
	@Inject
	public LogoutActionHandler(final Provider<HttpServletRequest> httpRequest) {
		this.httpRequest=httpRequest;
	}

	@Override
	public LogoutActionResult execute(LogoutAction action,
			BaseResult actionResult, ExecutionContext execContext)
			throws ActionException {
		
		LogoutActionResult result = (LogoutActionResult)actionResult;
		
		HttpSession session =httpRequest.get().getSession(false);
		
		if(session!=null){
			session.invalidate();
		}
		
		return result;
	}

	@Override
	public void undo(LogoutAction action, LogoutActionResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<LogoutAction> getActionType() {
		return LogoutAction.class;
	}
}
