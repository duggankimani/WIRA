package com.duggan.workflow.shared.requests;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.shared.responses.BaseResult;
import com.duggan.workflow.shared.responses.GetTaskListResult;

import java.lang.String;

public class GetTaskList extends BaseRequest<GetTaskListResult> {

	private String userId;
	private TaskType type;
	
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
	public BaseResult createDefaultActionResponse() {
		
		return new GetTaskListResult();
	}

}
