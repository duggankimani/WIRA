package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Doc;

public class GetTaskListResult extends BaseListResponse {

	private ArrayList<Doc> tasks;

	@SuppressWarnings("unused")
	public GetTaskListResult() {
	}

	public GetTaskListResult(ArrayList<Doc> tasks) {
		this.tasks = tasks;
	}

	public ArrayList<Doc> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Doc> tasks) {
		this.tasks = tasks;
	}
}
