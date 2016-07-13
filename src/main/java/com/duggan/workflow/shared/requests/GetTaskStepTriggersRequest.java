package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.TriggerType;
import com.duggan.workflow.shared.responses.GetTaskStepTriggersResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetTaskStepTriggersRequest extends BaseRequest<GetTaskStepTriggersResponse> {

	private Long taskStepId;
	private TriggerType triggerType;

	public GetTaskStepTriggersRequest() {
	}
	
	public GetTaskStepTriggersRequest(Long taskStepId,TriggerType triggerType) {
		this.taskStepId = taskStepId;
		this.triggerType = triggerType;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetTaskStepTriggersResponse();
	}

	public Long getTaskStepId() {
		return taskStepId;
	}

	public TriggerType getTriggerType() {
		return triggerType;
	}
}
