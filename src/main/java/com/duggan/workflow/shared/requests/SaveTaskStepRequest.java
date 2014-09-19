package com.duggan.workflow.shared.requests;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveTaskStepResponse;

public class SaveTaskStepRequest extends BaseRequest<SaveTaskStepResponse> {

	List<TaskStepDTO> steps = new ArrayList<TaskStepDTO>();
	
	public SaveTaskStepRequest() {
		
	}
	
	public SaveTaskStepRequest(List<TaskStepDTO> steps){
		this.steps.addAll(steps);
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new SaveTaskStepResponse();
	}

	public List<TaskStepDTO> getSteps() {
		return steps;
	}
}
