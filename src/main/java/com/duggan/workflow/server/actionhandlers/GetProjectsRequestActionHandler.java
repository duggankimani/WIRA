package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.RepositoryDaoHelper;
import com.duggan.workflow.shared.requests.GetProjectsRequest;
import com.duggan.workflow.shared.responses.GetProjectsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetProjectsRequestActionHandler extends
		AbstractActionHandler<GetProjectsRequest, GetProjectsResponse> {

	@Inject
	public GetProjectsRequestActionHandler() {
	}

	@Override
	public void execute(GetProjectsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetProjectsResponse response = (GetProjectsResponse)actionResult;
		
		response.setProjects(new RepositoryDaoHelper().getProjects());
	}
	
	@Override
	public Class<GetProjectsRequest> getActionType() {
		return GetProjectsRequest.class;
	}
}
