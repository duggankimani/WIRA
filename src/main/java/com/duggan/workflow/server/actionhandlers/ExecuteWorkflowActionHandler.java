package com.duggan.workflow.server.actionhandlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.task.Task;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.GridValue;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.requests.ExecuteTriggersRequest;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.ExecuteWorkflowResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.shared.ServiceException;
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
		Map<String, Value> values = execAfterStepTriggers(action,execContext);
		
		if(values!=null){
			long processInstanceId=0L;
			Task task = JBPMHelper.get().getTaskClient().getTask(action.getTaskId());
			processInstanceId = task.getTaskData().getProcessInstanceId();
						
			Document document = DocumentDaoHelper.getDocumentByProcessInstance(processInstanceId,false);
			//Doc document = JBPMHelper.get().getTask(action.getTaskId());
			assert document!=null;
			
			for(String key: values.keySet()){
				Value value = values.get(key);	
				Object val = value==null?null: value.getValue();
				vals.put(key, val);
				if(key!=null){
					//key = mappings.getOutputName(key);
					
					if(value instanceof GridValue){
						document.setDetails(key,((GridValue)value).getValue());
					}else{
						document.setValue(key,value);
					}
					
					
					log.debug("ExecuteWorkflowActionHandler.documentOut "+key+"="+val);
				}
			}
		
			//ProcessMappings mappings = JBPMHelper.get().getProcessDataMappings(action.getTaskId());
			//The Task Output Parameter Name mapped to Parameter Name document. 
			String docOutputName = "documentOut";//mappings.getOutputName("document");
			
			if(docOutputName!=null){
				log.debug("Task Output Parameter Mapping '"+docOutputName+"' -(mappedto)>  'document' ");
				vals.put(docOutputName, document);
			}
			
		}
		
		JBPMHelper.get().execute(action.getTaskId(),action.getUserId(), action.getAction(), vals);
		
		ExecuteWorkflowResult result = (ExecuteWorkflowResult)actionResult;		
		result.setDocument(JBPMHelper.get().getSummary(action.getTaskId()));
	}

	private Map<String, Value> execAfterStepTriggers(ExecuteWorkflow action,
			ExecutionContext execContext) throws ActionException{
		//Only for complete
		if(!action.getAction().equals(Actions.COMPLETE)){
			return action.getValues();
		}
		
		
		//List of steps
		List<TaskStepDTO> steps = ProcessDefHelper.getTaskStepsByTaskId(action.getTaskId());
		if(steps==null || steps.isEmpty()){
			return action.getValues();
		}
		
		//there are steps
		TaskStepDTO lastStep = steps.get(steps.size()-1);
		Long stepId = lastStep.getId();
		Long nextStepId = null;
		
		HTask task = new HTask(action.getTaskId());
		Map<String, Value> values  = action.getValues();
		for(String key: values.keySet()){
			Value value = values.get(key);	
			if(key!=null){
				//key = mappings.getOutputName(key);
				
				if(value instanceof GridValue){
					task.setDetails(key,((GridValue)value).getValue());
				}else{
					task.setValue(key,value);
				}
			}
		}
		ExecuteTriggersRequest execTrigger = new ExecuteTriggersRequest(stepId, nextStepId, task);
		
		Doc rtn = null;
		try{
			rtn = execContext.execute(execTrigger).getDocument();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		return rtn.getValues();
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
