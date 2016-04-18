package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.AssignmentDto;
import com.duggan.workflow.shared.requests.GetAssignmentRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetAssignmentResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetAssignmentRequestHandler extends
		AbstractActionHandler<GetAssignmentRequest, GetAssignmentResponse> {

	@Inject
	public GetAssignmentRequestHandler() {
	}

	@Override
	public void execute(GetAssignmentRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		AssignmentDto assignment = 
				ProcessDefHelper.getAssignment(action.getProcessRefId(),action.getNodeId());
						
		((GetAssignmentResponse)actionResult).setAssignment(assignment);
	}

	@Override
	public Class<GetAssignmentRequest> getActionType() {
		return GetAssignmentRequest.class;
	}
}