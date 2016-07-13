package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.TaskStepTrigger;
import com.wira.commons.shared.response.BaseResponse;

public class GetTaskStepTriggersResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<TaskStepTrigger> triggers = new ArrayList<TaskStepTrigger>();
	
	public GetTaskStepTriggersResponse() {
	}

	public ArrayList<TaskStepTrigger> getTriggers() {
		return triggers;
	}

	public void setTriggers(ArrayList<TaskStepTrigger> triggers) {
		this.triggers = triggers;
	}
	
	public void addTaskStepTrigger(TaskStepTrigger trigger){
		triggers.add(trigger);
	}
}
