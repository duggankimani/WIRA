package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.dao.helper.OrganizationDaoHelper;
import com.duggan.workflow.shared.model.Org;
import com.duggan.workflow.shared.requests.GetOrgsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetOrgsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetOrgsRequestHandler extends
		AbstractActionHandler<GetOrgsRequest, GetOrgsResponse> {

	@Inject
	public GetOrgsRequestHandler() {
	}

	@Override
	public void execute(GetOrgsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		GetOrgsResponse response = (GetOrgsResponse) actionResult;
		response.setOrgs((ArrayList<Org>) OrganizationDaoHelper.getAllOrgModels(action.getSearchText(),
				action.getOffset(), action.getLength()));
		response.setTotalCount(OrganizationDaoHelper.getCount(action.getSearchText()));
	}

	@Override
	public Class<GetOrgsRequest> getActionType() {
		return GetOrgsRequest.class;
	}
}
