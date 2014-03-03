package com.duggan.workflow.server.actionhandlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.duggan.workflow.client.util.Definitions;
import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.VersionManager;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.requests.GetContextRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetContextRequestResult;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import static com.duggan.workflow.server.ServerConstants.*;

public class GetContextRequestActionHandler extends 
		BaseActionHandler<GetContextRequest, GetContextRequestResult> {

	private final Provider<HttpServletRequest> httpRequest;

	@Inject
	public GetContextRequestActionHandler(Provider<HttpServletRequest> httpRequest) {
		this.httpRequest= httpRequest;
	}

	@Override
	public void execute(GetContextRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		HttpSession session = httpRequest.get().getSession(false);
		
		
		//Object sessionid=session.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
		Object user = session.getAttribute(ServerConstants.USER);

		GetContextRequestResult result = (GetContextRequestResult)actionResult;
		result.setIsValid(session!=null && user!=null);
		
		if(result.getIsValid()){
			result.setUser((HTUser)user);
			result.setGroups(LoginHelper.get().getGroupsForUser(result.getUser().getUserId()));
		}
		
		result.setVersion(VersionManager.getVersion());
				
	}

	@Override
	public void undo(GetContextRequest action, GetContextRequestResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetContextRequest> getActionType() {
		return GetContextRequest.class;
	}
}
