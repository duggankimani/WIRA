package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;

public class AssignTaskRequest extends BaseRequest<BaseResponse> {

	private Long taskId;
	private String userId;

	@SuppressWarnings("unused")
	private AssignTaskRequest() {
		// For serialization only
	}

	public AssignTaskRequest(Long taskId, String userId) {
		this.taskId = taskId;
		this.userId = userId;
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
		return new BaseResponse();
	}
}
