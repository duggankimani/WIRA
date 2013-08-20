package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.HTSummary;

import java.util.List;

public class GetTaskListResult extends BaseResponse {

	private List<DocSummary> tasks;

	@SuppressWarnings("unused")
	public GetTaskListResult() {
	}

	public GetTaskListResult(List<DocSummary> tasks) {
		this.tasks = tasks;
	}

	public List<DocSummary> getTasks() {
		return tasks;
	}

	public void setTasks(List<DocSummary> tasks) {
		this.tasks = tasks;
	}
}
