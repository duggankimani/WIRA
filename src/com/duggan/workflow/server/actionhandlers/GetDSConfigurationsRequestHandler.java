package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.DSConfigHelper;
import com.duggan.workflow.shared.requests.GetDSConfigurationsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDSConfigurationsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetDSConfigurationsRequestHandler
		extends
		BaseActionHandler<GetDSConfigurationsRequest, GetDSConfigurationsResponse> {

	@Inject
	public GetDSConfigurationsRequestHandler() {
	}

	@Override
	public void execute(GetDSConfigurationsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetDSConfigurationsResponse response = (GetDSConfigurationsResponse)actionResult;
		
		response.setConfigurations(DSConfigHelper.getConfigurations());
	}
	
	@Override
	public Class<GetDSConfigurationsRequest> getActionType() {
		return GetDSConfigurationsRequest.class;
	}
}
