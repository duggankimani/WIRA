package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.ProcessLog;
import com.duggan.workflow.shared.model.TaskLog;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;
import com.wira.commons.shared.models.Version;
import com.wira.commons.shared.response.BaseResponse;

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
