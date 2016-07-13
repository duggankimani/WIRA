package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.shared.model.AssignmentDto;
import com.duggan.workflow.shared.requests.GetAssignmentRequest;
import com.duggan.workflow.shared.responses.GetAssignmentResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetAssignmentRequestHandler extends
		AbstractActionHandler<GetAssignmentRequest, GetAssignmentResponse> {

	@Inject
	public GetAssignmentRequestHandler() {
	}

	@Override
	public void execute(GetAssignmentRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		AssignmentDto assignment = 
				ProcessDaoHelper.getAssignment(action.getProcessRefId(),action.getNodeId());
						
		((GetAssignmentResponse)actionResult).setAssignment(assignment);
	}

	@Override
	public Class<GetAssignmentRequest> getActionType() {
		return GetAssignmentRequest.class;
	}
}
