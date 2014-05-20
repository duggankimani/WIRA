package com.duggan.workflow.shared.requests;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.shared.responses.BaseResponse;

public class GetAlertCountResult extends BaseResponse {

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
