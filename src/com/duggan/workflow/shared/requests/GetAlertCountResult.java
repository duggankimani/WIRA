package com.duggan.workflow.shared.requests;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.shared.responses.BaseResult;
import com.gwtplatform.dispatch.shared.Result;
import java.util.HashMap;

public class GetAlertCountResult extends BaseResult {

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
