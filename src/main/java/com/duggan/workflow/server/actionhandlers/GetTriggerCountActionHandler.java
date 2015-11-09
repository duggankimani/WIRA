package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.TriggerType;
import com.duggan.workflow.shared.requests.GetTriggerCountRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTriggerCountResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetTriggerCountActionHandler extends
		AbstractActionHandler<GetTriggerCountRequest, GetTriggerCountResponse> {

	@Inject
	public GetTriggerCountActionHandler() {
	}

	@Override
	public void execute(GetTriggerCountRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		GetTriggerCountResponse countResult = (GetTriggerCountResponse)actionResult;
		countResult.addCount(TriggerType.BEFORESTEP, 
				ProcessDefHelper.getTriggerCount(action.getTaskStepId(), TriggerType.BEFORESTEP));
		countResult.addCount(TriggerType.AFTERSTEP, 
				ProcessDefHelper.getTriggerCount(action.getTaskStepId(), TriggerType.AFTERSTEP));
		
	}
	
	@Override
	public Class<GetTriggerCountRequest> getActionType() {

		return GetTriggerCountRequest.class;
	}
	
}
