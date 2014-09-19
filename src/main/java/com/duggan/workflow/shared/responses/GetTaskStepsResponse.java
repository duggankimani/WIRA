package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.TaskStepDTO;

public class GetTaskStepsResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<TaskStepDTO> steps;
	
	public GetTaskStepsResponse() {
	}

	public List<TaskStepDTO> getSteps() {
		return steps;
	}

	public void setSteps(List<TaskStepDTO> steps) {
		this.steps = steps;
	}
	
}
