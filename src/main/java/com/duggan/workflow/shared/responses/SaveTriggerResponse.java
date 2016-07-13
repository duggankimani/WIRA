package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.Trigger;
import com.wira.commons.shared.response.BaseResponse;

public class SaveTriggerResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Trigger trigger;

	public SaveTriggerResponse() {
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

}
