package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.shared.model.AssignmentDto;
import com.duggan.workflow.shared.requests.SaveAssignmentRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveAssignmentResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveAssignmentRequestHandler extends
		AbstractActionHandler<SaveAssignmentRequest, SaveAssignmentResponse> {

	@Inject
	public SaveAssignmentRequestHandler() {
	}

	@Override
	public void execute(SaveAssignmentRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		AssignmentDto assignment = action.getAssignmentDto();
		AssignmentDto dto = ProcessDaoHelper.create(assignment);
		((SaveAssignmentResponse)actionResult).setAssignment(assignment);
	}

	@Override
	public Class<SaveAssignmentRequest> getActionType() {
		return SaveAssignmentRequest.class;
	}
}
