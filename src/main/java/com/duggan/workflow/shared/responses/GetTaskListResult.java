package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.Doc;

public class GetTaskListResult extends BaseResponse {

	private List<Doc> tasks;

	@SuppressWarnings("unused")
	public GetTaskListResult() {
	}

	public GetTaskListResult(List<Doc> tasks) {
		this.tasks = tasks;
	}

	public List<Doc> getTasks() {
		return tasks;
	}

	public void setTasks(List<Doc> tasks) {
		this.tasks = tasks;
	}
}
