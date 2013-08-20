package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.HTask;

public class GetTaskResult extends BaseResponse {

	private HTask task;

	@SuppressWarnings("unused")
	public GetTaskResult() {
		// For serialization only
	}

	public GetTaskResult(HTask task) {
		this.task = task;
	}

	public HTask getTask() {
		return task;
	}

	public void setTask(HTask task) {
		this.task = task;
	}
}
