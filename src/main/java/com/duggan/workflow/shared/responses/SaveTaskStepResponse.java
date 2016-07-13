package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.TaskStepDTO;
import com.wira.commons.shared.response.BaseResponse;


public class SaveTaskStepResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ArrayList<TaskStepDTO> ArrayList = new ArrayList<TaskStepDTO>();
	
	public SaveTaskStepResponse() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<TaskStepDTO> getList() {
		return ArrayList;
	}

	public void setList(ArrayList<TaskStepDTO> ArrayList) {
		this.ArrayList = ArrayList;
	}
	
	
}
