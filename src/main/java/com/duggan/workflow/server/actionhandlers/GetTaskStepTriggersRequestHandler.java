package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.TaskStepTrigger;
import com.duggan.workflow.shared.requests.GetTaskStepTriggersRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTaskStepTriggersResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetTaskStepTriggersRequestHandler extends
	BaseActionHandler<GetTaskStepTriggersRequest, GetTaskStepTriggersResponse>{

	@Inject
	public GetTaskStepTriggersRequestHandler() {
	}
	
	@Override
	public void execute(GetTaskStepTriggersRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		List<TaskStepTrigger> triggers =  
				ProcessDefHelper.getTaskStepTriggers(action.getTaskStepId(),action.getTriggerType());
		
		((GetTaskStepTriggersResponse)actionResult).setTriggers(triggers);
	}
	
	public java.lang.Class<GetTaskStepTriggersRequest> getActionType() {
		return GetTaskStepTriggersRequest.class;
	};
}
