package com.duggan.workflow.shared.requests;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTaskListResult;

import java.lang.String;

public class GetTaskList extends BaseRequest<GetTaskListResult> {

	private String userId;
	private TaskType type;
	private Long processInstanceId;
	
	@SuppressWarnings("unused")
	private GetTaskList() {
	}

	public GetTaskList(String userId, TaskType type) {
		this.userId = userId;
		this.type = type;
		
	}

	public String getUserId() {
		return userId;
	}
	
	public TaskType getType() {
		return type;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetTaskListResult();
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

}
