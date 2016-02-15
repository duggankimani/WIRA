package com.duggan.workflow.shared.requests;

import java.util.HashMap;

import com.duggan.workflow.shared.model.TaskType;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetAlertCountResult;

public class GetAlertCount extends BaseRequest<GetAlertCountResult> {

	private String userId;
	
	public GetAlertCount() {
	}
	
	public GetAlertCount(String userId) {
		this.userId = userId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetAlertCountResult(new HashMap<TaskType, Integer>());
	}

	public String getUserId() {
		return userId;
	}
}
