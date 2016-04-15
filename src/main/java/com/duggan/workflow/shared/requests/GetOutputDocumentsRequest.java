package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetOutputDocumentsResponse;

public class GetOutputDocumentsRequest extends BaseRequest<GetOutputDocumentsResponse> {

	private Long documentId;
	private String processRefId;

	public GetOutputDocumentsRequest() {
	}
	
	public GetOutputDocumentsRequest(Long id) {
		this.documentId = id;
	}
	
	public GetOutputDocumentsRequest(String processRefId) {
		this.processRefId = processRefId;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetOutputDocumentsResponse();
	}

	public String getProcessRefId() {
		return processRefId;
	}

	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}

}
