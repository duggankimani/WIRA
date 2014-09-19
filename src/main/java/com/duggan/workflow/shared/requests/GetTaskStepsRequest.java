package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTaskStepsResponse;

public class GetTaskStepsRequest extends BaseRequest<GetTaskStepsResponse> {
	
	private String processId;
	private Long nodeId;
	
	public GetTaskStepsRequest() {
		
	}
	
	public GetTaskStepsRequest(String processId,Long nodeId) {
		this.processId = processId;
		this.nodeId = nodeId;
	}

	public String getProcessId() {
		return processId;
	}

	public Long getNodeId() {
		return nodeId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetTaskStepsResponse();
	}
}
