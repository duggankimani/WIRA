package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTaskNodesResponse;

public class GetTaskNodesRequest extends BaseRequest<GetTaskNodesResponse> {

	private String processId;

	public GetTaskNodesRequest() {
	}
	
	public GetTaskNodesRequest(String processId) {
		this.processId = processId;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetTaskNodesResponse();
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

}
