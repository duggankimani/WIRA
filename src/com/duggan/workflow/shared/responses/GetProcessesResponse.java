package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.ProcessDef;
import java.util.List;

public class GetProcessesResponse extends BaseResponse {

	private List<ProcessDef> processes;

	public GetProcessesResponse() {
		// For serialization only
	}

	public GetProcessesResponse(List<ProcessDef> processes) {
		this.processes = processes;
	}

	public List<ProcessDef> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessDef> processes) {
		this.processes = processes;
	}
}
