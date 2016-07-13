package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.AssignmentDto;
import com.wira.commons.shared.response.BaseResponse;


public class GetAssignmentResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AssignmentDto assignment;

	public GetAssignmentResponse() {
		// For serialization only
	}

	public AssignmentDto getAssignment() {
		return assignment;
	}

	public void setAssignment(AssignmentDto assignment) {
		this.assignment = assignment;
	}

}
