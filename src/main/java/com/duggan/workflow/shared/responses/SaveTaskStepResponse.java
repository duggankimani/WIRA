package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.TaskStepDTO;


public class SaveTaskStepResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<TaskStepDTO> list = new ArrayList<TaskStepDTO>();
	
	public SaveTaskStepResponse() {
		// TODO Auto-generated constructor stub
	}

	public List<TaskStepDTO> getList() {
		return list;
	}

	public void setList(List<TaskStepDTO> list) {
		this.list = list;
	}
	
	
}
