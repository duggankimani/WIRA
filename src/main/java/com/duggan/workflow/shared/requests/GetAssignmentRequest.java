package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetAssignmentResponse;

public class GetAssignmentRequest extends BaseRequest<GetAssignmentResponse> {

	private String processRefId;
	private Long nodeId;

	public GetAssignmentRequest() {
	}

	public GetAssignmentRequest(String processRefId, Long nodeId) {
		this.processRefId = processRefId;
		this.nodeId = nodeId;
	}

	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetAssignmentResponse();
	}

	public String getProcessRefId() {
		return processRefId;
	}

	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

}
