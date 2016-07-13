package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.TaskStepTrigger;
import com.wira.commons.shared.response.BaseResponse;

public class SaveTaskStepTriggerResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TaskStepTrigger trigger;

	public SaveTaskStepTriggerResponse() {
	}

	public TaskStepTrigger getTrigger() {
		return trigger;
	}

	public void setTaskStepTrigger(TaskStepTrigger trigger) {
		this.trigger = trigger;
	}

}


