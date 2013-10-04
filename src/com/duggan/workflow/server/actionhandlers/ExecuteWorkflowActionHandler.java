package com.duggan.workflow.server.actionhandlers;

import java.util.HashMap;
import java.util.Map;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.ExecuteWorkflowResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class ExecuteWorkflowActionHandler extends
		BaseActionHandler<ExecuteWorkflow, ExecuteWorkflowResult> {

	@Inject
	public ExecuteWorkflowActionHandler() {
	}

	@Override
	public void execute(ExecuteWorkflow action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Map<String, Object> vals = new HashMap<>();
		
		Map<String, Value> values = action.getValues();
		if(values!=null){
			for(String key: values.keySet()){
				vals.put(key, values.get(key)==null?null: values.get(key).getValue());
			}
		}
		
		JBPMHelper.get().execute(action.getTaskId(),action.getUserId(), action.getAction(), vals);
		
		ExecuteWorkflowResult result = (ExecuteWorkflowResult)actionResult;
		
		result.setDocument(JBPMHelper.get().getSummary(action.getTaskId()));
	}

	@Override
	public void undo(ExecuteWorkflow action, ExecuteWorkflowResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<ExecuteWorkflow> getActionType() {
		return ExecuteWorkflow.class;
	}
}
