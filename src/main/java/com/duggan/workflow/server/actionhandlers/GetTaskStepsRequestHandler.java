package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.requests.GetTaskStepsRequest;
import com.duggan.workflow.shared.responses.GetTaskStepsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetTaskStepsRequestHandler extends
		AbstractActionHandler<GetTaskStepsRequest, GetTaskStepsResponse> {
	
	@Inject
	public GetTaskStepsRequestHandler() {
	}
	
	@Override
	public void execute(GetTaskStepsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		List<TaskStepDTO> steps = new ArrayList<>();
		if(action.getProcessId()==null){
			if(action.getTaskId()!=null){
				steps = ProcessDaoHelper.getTaskStepsByTaskId(action.getTaskId());
			}else if(action.getDocumentId()!=null){
				steps = ProcessDaoHelper.getTaskStepsByDocumentId(action.getDocumentId());
			}
		}else{
			steps = ProcessDaoHelper.getSteps(action.getProcessId(), action.getNodeId());
		}
		
		((GetTaskStepsResponse)actionResult).setSteps((ArrayList<TaskStepDTO>) steps);
		
	}
	
	public java.lang.Class<GetTaskStepsRequest> getActionType() {
		return GetTaskStepsRequest.class;
	};

}
