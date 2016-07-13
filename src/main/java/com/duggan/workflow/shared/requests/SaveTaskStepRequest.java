package com.duggan.workflow.shared.requests;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.responses.SaveTaskStepResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class SaveTaskStepRequest extends BaseRequest<SaveTaskStepResponse> {

	ArrayList<TaskStepDTO> steps = new ArrayList<TaskStepDTO>();
	
	public SaveTaskStepRequest() {
		
	}
	
	public SaveTaskStepRequest(ArrayList<TaskStepDTO> steps){
		this.steps.addAll(steps);
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new SaveTaskStepResponse();
	}

	public ArrayList<TaskStepDTO> getSteps() {
		return steps;
	}
}
