package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.Organization;
import com.duggan.workflow.shared.requests.GetAllOganizationsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetAllOrganizationsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetAllOrganizationsRequestActionHandler extends
		AbstractActionHandler<GetAllOganizationsRequest, GetAllOrganizationsResponse> {

	@Inject
	public GetAllOrganizationsRequestActionHandler() {
	}
	
	@Override
	public void execute(GetAllOganizationsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		List<Organization> organizations = LoginHelper.getHelper().getAllOrganizations();
		GetAllOrganizationsResponse result = (GetAllOrganizationsResponse)actionResult;
	
		result.setOrganizations(organizations);
	}
	
	
	@Override
	public Class<GetAllOganizationsRequest> getActionType() {
		return GetAllOganizationsRequest.class;
	}
}
