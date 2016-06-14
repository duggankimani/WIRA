package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.ProcessDef;

public class GetProcessesResponse extends BaseResponse {

	private ArrayList<ProcessDef> processes;

	public GetProcessesResponse() {
		// For serialization only
	}

	public GetProcessesResponse(ArrayList<ProcessDef> processes) {
		this.processes = processes;
	}

	public ArrayList<ProcessDef> getProcesses() {
		return processes;
	}

	public void setProcesses(ArrayList<ProcessDef> processes) {
		this.processes = processes;
	}
}
