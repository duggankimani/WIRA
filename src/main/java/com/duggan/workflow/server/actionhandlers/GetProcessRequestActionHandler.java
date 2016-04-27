package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.GetProcessRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetProcessRequestActionHandler extends
		AbstractActionHandler<GetProcessRequest, GetProcessResponse> {

	@Inject
	public GetProcessRequestActionHandler() {
	}
	
	@Override
	public void execute(GetProcessRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Long processDefId = action.getProcessDefId();
		
		ProcessDef process = null;
		if(processDefId!=null){
			process = ProcessDefHelper.getProcessDef(processDefId);
		}else if(action.getProcessRefId()!=null){
			process = ProcessDefHelper.getProcessDef(action.getProcessRefId());
		}
		
		GetProcessResponse response = (GetProcessResponse)actionResult;
		
		response.setProcessDef(process);
	}
	
	@Override
	public Class<GetProcessRequest> getActionType() {
		return GetProcessRequest.class;
	}
}
