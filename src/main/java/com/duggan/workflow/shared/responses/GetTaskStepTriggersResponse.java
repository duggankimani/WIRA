package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.TaskStepTrigger;

public class GetTaskStepTriggersResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<TaskStepTrigger> triggers = new ArrayList<TaskStepTrigger>();
	
	public GetTaskStepTriggersResponse() {
	}

	public List<TaskStepTrigger> getTriggers() {
		return triggers;
	}

	public void setTriggers(List<TaskStepTrigger> triggers) {
		this.triggers = triggers;
	}
	
	public void addTaskStepTrigger(TaskStepTrigger trigger){
		triggers.add(trigger);
	}
}
