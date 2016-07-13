package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.dashboard.LongTask;
import com.wira.commons.shared.response.BaseResponse;

public class GetLongTasksResponse extends BaseResponse {

	private ArrayList<LongTask> longTasks;

	public GetLongTasksResponse() {
		// For serialization only
	}

	public ArrayList<LongTask> getLongTasks() {
		return longTasks;
	}

	public void setLongTasks(ArrayList<LongTask> longTasks) {
		this.longTasks = longTasks;
	}
}
