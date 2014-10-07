package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.TaskStepTrigger;
import com.duggan.workflow.shared.requests.SaveTaskStepTriggerRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveTaskStepTriggerResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveTaskStepTriggerRequestHandler 
	extends BaseActionHandler<SaveTaskStepTriggerRequest, SaveTaskStepTriggerResponse> {
	
	@Inject
	public SaveTaskStepTriggerRequestHandler() {
	}
	
	@Override
	public void execute(SaveTaskStepTriggerRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		TaskStepTrigger trigger = ProcessDefHelper.saveTaskStepTrigger(action.getTaskStepTrigger());
		((SaveTaskStepTriggerResponse)actionResult).setTaskStepTrigger(trigger);
	}
	
	@Override
	public Class<SaveTaskStepTriggerRequest> getActionType() {
		return SaveTaskStepTriggerRequest.class;
	}
}
