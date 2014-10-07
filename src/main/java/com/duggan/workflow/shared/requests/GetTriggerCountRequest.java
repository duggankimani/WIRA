package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTriggerCountResponse;

public class GetTriggerCountRequest extends BaseRequest<GetTriggerCountResponse> {

	private Long taskStepId;
	
	public GetTriggerCountRequest() {
	}
	
	public GetTriggerCountRequest(Long taskStepId) {
		this.taskStepId = taskStepId;
	}
	
	public Long getTaskStepId() {
		return taskStepId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetTriggerCountResponse();
	}

}
