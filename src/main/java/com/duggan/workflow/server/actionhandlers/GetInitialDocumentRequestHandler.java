package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.requests.ExecuteTriggersRequest;
import com.duggan.workflow.shared.requests.GetDocumentRequest;
import com.duggan.workflow.shared.requests.GetInitialDocumentRequest;
import com.duggan.workflow.shared.requests.GetTaskStepsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDocumentResult;
import com.duggan.workflow.shared.responses.GetInitialDocumentResponse;
import com.duggan.workflow.shared.responses.GetTaskStepsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * 
 * @author duggan
 *
 */
public class GetInitialDocumentRequestHandler extends
		AbstractActionHandler<GetInitialDocumentRequest, GetInitialDocumentResponse> {

	@Inject
	public GetInitialDocumentRequestHandler() {
	}

	@Override
	public void execute(GetInitialDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		try{
			
			GetDocumentRequest request = new GetDocumentRequest(action.getDocRefId(), action.getTaskId(),action.isLoadAsAdmin());
			GetDocumentResult result = execContext.execute(request);
			Doc doc = result.getDoc(); 
			
			GetTaskStepsRequest taskSteps = new GetTaskStepsRequest(doc.getDocumentId(), action.getTaskId());
			GetTaskStepsResponse response = execContext.execute(taskSteps);
			
			List<TaskStepDTO> steps = response.getSteps();
			if(steps!=null && !steps.isEmpty()){
				//Execute initial
				ExecuteTriggersRequest execTrigger = new ExecuteTriggersRequest(-1L, steps.get(0).getId(), doc);
				doc = execContext.execute(execTrigger).getDocument();			
			}
			
			GetInitialDocumentResponse finalResult = (GetInitialDocumentResponse)actionResult;
			finalResult.setSteps((ArrayList<TaskStepDTO>) steps);
			finalResult.setDoc(doc);
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void undo(GetInitialDocumentRequest action, GetInitialDocumentResponse result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetInitialDocumentRequest> getActionType() {
		return GetInitialDocumentRequest.class;
	}
}
