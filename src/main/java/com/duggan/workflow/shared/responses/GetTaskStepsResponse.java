package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.TaskStepDTO;
import com.wira.commons.shared.response.BaseResponse;

public class GetTaskStepsResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<TaskStepDTO> steps;
	private ArrayList<String> conditionalFields;
	
	public GetTaskStepsResponse() {
	}

	public ArrayList<TaskStepDTO> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<TaskStepDTO> steps) {
		this.steps = steps;
	}

	public ArrayList<String> getConditionalFields() {
		return conditionalFields;
	}

	public void setConditionalFields(ArrayList<String> conditionalFields) {
		this.conditionalFields = conditionalFields;
	}
	
}
