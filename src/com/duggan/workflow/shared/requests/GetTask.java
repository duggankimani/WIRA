package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTaskResult;

import java.lang.String;
import java.lang.Long;

public class GetTask extends BaseRequest<GetTaskResult> {

	private String userId;
	private Long taskId;

	@SuppressWarnings("unused")
	private GetTask() {
		// For serialization only
	}
	
	public GetTask(String userId, Long taskId) {
		this.userId = userId;
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public Long getTaskId() {
		return taskId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetTaskResult();
	}
}
