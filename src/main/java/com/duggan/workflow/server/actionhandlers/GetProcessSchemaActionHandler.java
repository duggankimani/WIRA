package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.shared.model.Schema;
import com.duggan.workflow.shared.requests.GetProcessSchemaRequest;
import com.duggan.workflow.shared.responses.GetProcessSchemaResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetProcessSchemaActionHandler extends
		AbstractActionHandler<GetProcessSchemaRequest, GetProcessSchemaResponse> {

	@Inject
	public GetProcessSchemaActionHandler() {
	}

	@Override
	public void execute(GetProcessSchemaRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		List<Schema> processes = ProcessDaoHelper.getProcessSchema(action.getProcessRefId());
		
		GetProcessSchemaResponse response = (GetProcessSchemaResponse)actionResult;
		
		response.setSchema((ArrayList<Schema>) processes);
	}

	@Override
	public Class<GetProcessSchemaRequest> getActionType() {
		return GetProcessSchemaRequest.class;
	}
}
