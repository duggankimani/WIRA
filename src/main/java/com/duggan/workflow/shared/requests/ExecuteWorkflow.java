package com.duggan.workflow.shared.requests;

import java.util.HashMap;

import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.responses.ExecuteWorkflowResult;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

/**
 * 
 * @author duggan
 *
 */
public class ExecuteWorkflow extends BaseRequest<ExecuteWorkflowResult> {

	private Actions action;
	private Long taskId;
	private String userId;
	private HashMap<String, Value> values = new HashMap<String, Value>();
	
	
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
	public BaseResponse createDefaultActionResponse() {
		
		return new ExecuteWorkflowResult();
	}

	public void addValue(String name, Value value){
		values.put(name, value);
	}
	
	public void clear(){
		values.clear();
	}
	
	public HashMap<String, Value> getValues() {
		return values;
	}

	public void setValues(HashMap<String, Value> values) {
		this.values = values;
	}
}
