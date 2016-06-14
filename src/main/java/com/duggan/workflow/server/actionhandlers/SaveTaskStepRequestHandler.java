package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.requests.SaveTaskStepRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveTaskStepResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveTaskStepRequestHandler extends
		AbstractActionHandler<SaveTaskStepRequest, SaveTaskStepResponse> {

	@Inject
	public SaveTaskStepRequestHandler() {
	}
	
	@Override
	public void execute(SaveTaskStepRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		ProcessDefHelper.createTaskSteps(action.getSteps());
		
		TaskStepDTO dto = action.getSteps().get(0);
		
		ProcessDefModel model = DB.getProcessDao().getProcessDef(dto.getProcessDefId());
		List<TaskStepDTO> allSteps = ProcessDefHelper.getSteps(model.getProcessId(), dto.getNodeId());
		((SaveTaskStepResponse)actionResult).setList((ArrayList<TaskStepDTO>) allSteps);
	}
	
	@Override
	public Class<SaveTaskStepRequest> getActionType() {
		return SaveTaskStepRequest.class;
	}
}
