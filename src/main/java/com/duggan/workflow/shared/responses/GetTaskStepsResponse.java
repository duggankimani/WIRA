package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.TaskStepDTO;

public class GetTaskStepsResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<TaskStepDTO> steps;
	
	public GetTaskStepsResponse() {
	}

	public ArrayList<TaskStepDTO> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<TaskStepDTO> steps) {
		this.steps = steps;
	}
	
}
