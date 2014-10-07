package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Trigger;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveTriggerResponse;

public class SaveTriggerRequest extends BaseRequest<SaveTriggerResponse> {

	private Trigger trigger;
	
	public SaveTriggerRequest() {
	}
	
	public SaveTriggerRequest(Trigger trigger){
		this.trigger = trigger;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveTriggerResponse();
	}
	
}
