package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.responses.GetTaskStepsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetTaskStepsRequest extends BaseRequest<GetTaskStepsResponse> {
	
	private String processId;
	private Long nodeId;
	
	private Doc doc;
	
	public GetTaskStepsRequest() {
	}
	
	public GetTaskStepsRequest(String processId,Long nodeId) {
		this.processId = processId;
		this.nodeId = nodeId;
	}
	
	public GetTaskStepsRequest(Doc doc){
		this.doc = doc;
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

	public Doc getDoc() {
		return doc;
	}

	public void setDoc(Doc doc) {
		this.doc = doc;
	}

}
