package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.TaskLog;

public class GetProcessLogResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<TaskLog> logs = new ArrayList<TaskLog>();

	public GetProcessLogResponse() {
	}

	public List<TaskLog> getLogs() {
		return logs;
	}

	public void setLogs(List<TaskLog> logs) {
		this.logs = logs;
	}

}
