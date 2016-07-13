package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetTriggerCountResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

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
