package com.duggan.workflow.shared.requests;

import java.util.HashMap;
import java.util.Map;

import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.ParamValue;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.ExecuteWorkflowResult;

/**
 * 
 * @author duggan
 *
 */
public class ExecuteWorkflow extends BaseRequest<ExecuteWorkflowResult> {

	private Actions action;
	private Long taskId;
	private String userId;
	private Map<String, ParamValue> values = new HashMap<String, ParamValue>();
	
	
	public ExecuteWorkflow(){
		
	}
	
	public ExecuteWorkflow(Long taskId, String userId, Actions action) {
		this.action = action;
		this.taskId=taskId;
		this.userId = userId;
	}

	public Actions getAction() {
		return action;
	}

	public void setAction(Actions action) {
		this.action = action;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public BaseResult createDefaultActionResponse() {
		
		return new ExecuteWorkflowResult();
	}

	public void addValue(String name, ParamValue value){
		values.put(name, value);
	}
	
	public void clear(){
		values.clear();
	}
	
	public Map<String, ParamValue> getValues() {
		return values;
	}

	public void setValues(Map<String, ParamValue> values) {
		this.values = values;
	}
}
