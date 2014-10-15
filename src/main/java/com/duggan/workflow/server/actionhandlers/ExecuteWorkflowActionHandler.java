package com.duggan.workflow.server.actionhandlers;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.task.Task;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.GridValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.ProcessMappings;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.ExecuteWorkflowResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
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
					
					
					log.warn("ExecuteWorkflowActionHandler.documentOut "+key+"="+val);
				}
			}
		
			//ProcessMappings mappings = JBPMHelper.get().getProcessDataMappings(action.getTaskId());
			//The Task Output Parameter Name mapped to Parameter Name document. 
			String docOutputName = "documentOut";//mappings.getOutputName("document");
			
			if(docOutputName!=null){
				log.info("Task Output Parameter Mapping '"+docOutputName+"' -(mappedto)>  'document' ");
				vals.put(docOutputName, document);
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
