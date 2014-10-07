package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.Trigger;
import com.duggan.workflow.shared.requests.SaveTriggerRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveTriggerResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveTriggerRequestHandler extends BaseActionHandler<SaveTriggerRequest, SaveTriggerResponse> {
	
	@Inject
	public SaveTriggerRequestHandler() {
	}
	
	@Override
	public void execute(SaveTriggerRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Trigger trigger = ProcessDefHelper.saveTrigger(action.getTrigger());
		((SaveTriggerResponse)actionResult).setTrigger(trigger);
	}
	
	@Override
	public Class<SaveTriggerRequest> getActionType() {
		return SaveTriggerRequest.class;
	}
}
