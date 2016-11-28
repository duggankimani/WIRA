package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.TaskLog;
import com.wira.commons.shared.response.BaseResponse;

public class GetProcessLogResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<TaskLog> logs = new ArrayList<TaskLog>();

	public GetProcessLogResponse() {
	}

	public ArrayList<TaskLog> getLogs() {
		return logs;
	}

	public void setLogs(ArrayList<TaskLog> logs) {
		this.logs = logs;
	}

}
