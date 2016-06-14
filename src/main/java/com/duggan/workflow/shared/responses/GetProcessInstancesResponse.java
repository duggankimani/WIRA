package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.ProcessLog;
import com.duggan.workflow.shared.model.TaskLog;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.model.Version;

public class GetProcessInstancesResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ProcessLog> logs = new ArrayList<ProcessLog>();

	public GetProcessInstancesResponse() {
	}

	public ArrayList<ProcessLog> getLogs() {
		return logs;
	}

	public void setLogs(ArrayList<ProcessLog> logs) {
		this.logs = logs;
	}

}
