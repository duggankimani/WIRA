package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetOutputDocumentsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetOutputDocumentsRequest extends BaseRequest<GetOutputDocumentsResponse> {

	private Long documentId;
	private String processRefId;
	private String searchTerm;

	public GetOutputDocumentsRequest() {
	}
	
	public GetOutputDocumentsRequest(Long documentId) {
		this.documentId = documentId;
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

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

}
