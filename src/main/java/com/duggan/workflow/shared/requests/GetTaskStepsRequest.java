package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetTaskStepsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetTaskStepsRequest extends BaseRequest<GetTaskStepsResponse> {
	
	private String processId;
	private Long nodeId;
	
	private Long documentId;
	private Long taskId;
	
	public GetTaskStepsRequest() {
	}
	
	public GetTaskStepsRequest(String processId,Long nodeId) {
		this.processId = processId;
		this.nodeId = nodeId;
	}
	
	public GetTaskStepsRequest(Long documentId, Long taskId){
		this.documentId = documentId;
		this.taskId = taskId;
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

	public Long getDocumentId() {
		return documentId;
	}

	public Long getTaskId() {
		return taskId;
	}

}
