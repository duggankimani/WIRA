package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.ExecuteTriggersResponse;

public class ExecuteTriggersRequest extends BaseRequest<ExecuteTriggersResponse> {

	private Long previousStepId;
	private Long nextStepId;
	private Doc doc;
	
	@SuppressWarnings("unused")
	private ExecuteTriggersRequest() {
	}

	public ExecuteTriggersRequest(Long previousStepId,Long nextStepId, Doc doc) {
		this.doc = doc;
		this.previousStepId=previousStepId;
		this.nextStepId = nextStepId;
	}
	
	public Long getPreviousStepId() {
		return previousStepId;
	}

	public Long getNextStepId() {
		return nextStepId;
	}

	public Doc getDoc() {
		return doc;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new ExecuteTriggersResponse();
	}
}
