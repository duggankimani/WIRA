package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.responses.ExecuteTriggerResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class ExecuteTriggerRequest extends BaseRequest<ExecuteTriggerResponse> {

	private String triggerName;
	private Doc doc;
	
	@SuppressWarnings("unused")
	private ExecuteTriggerRequest() {
	}

	public ExecuteTriggerRequest(String triggerName, Doc doc) {
		this.triggerName = triggerName;
		this.doc = doc;
	}

	public Doc getDoc() {
		return doc;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new ExecuteTriggerResponse();
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
}
