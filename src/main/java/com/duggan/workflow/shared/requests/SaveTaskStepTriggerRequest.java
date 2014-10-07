package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.TaskStepTrigger;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveTaskStepTriggerResponse;

public class SaveTaskStepTriggerRequest extends BaseRequest<SaveTaskStepTriggerResponse> {

	private TaskStepTrigger trigger;
	
	public SaveTaskStepTriggerRequest() {
	}
	
	public SaveTaskStepTriggerRequest(TaskStepTrigger trigger){
		this.trigger = trigger;
	}

	public TaskStepTrigger getTaskStepTrigger() {
		return trigger;
	}

	public void setTaskStepTrigger(TaskStepTrigger trigger) {
		this.trigger = trigger;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveTaskStepTriggerResponse();
	}
	
}
