package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.TaskNode;
import com.duggan.workflow.shared.requests.GetTaskNodesRequest;
import com.duggan.workflow.shared.responses.GetTaskNodesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetTaskNodesActionHandler extends AbstractActionHandler<GetTaskNodesRequest, GetTaskNodesResponse> {
	
	Logger log = Logger.getLogger(GetTaskNodesActionHandler.class);
	
	@Inject
	public GetTaskNodesActionHandler() {
	}
	
	@Override
	public void execute(GetTaskNodesRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		try{
			((GetTaskNodesResponse)actionResult).setTaskNodes((ArrayList<TaskNode>) JBPMHelper.get().getWorkflowProcessNodes(action.getProcessId()));
		}catch(Exception e){
			log.warn(e.getMessage());
		}
		
		
	}
	
	public java.lang.Class<GetTaskNodesRequest> getActionType() {
		return GetTaskNodesRequest.class;
	};

}
