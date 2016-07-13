package com.duggan.workflow.shared.responses;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.wira.commons.shared.response.BaseResponse;

public class GetAlertCountResult extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<TaskType, Integer> counts;

	@SuppressWarnings("unused")
	private GetAlertCountResult() {
		// For serialization only
	}

	public GetAlertCountResult(HashMap<TaskType, Integer> counts) {
		this.counts = counts;
	}

	public HashMap<TaskType, Integer> getCounts() {
		return counts;
	}
}
