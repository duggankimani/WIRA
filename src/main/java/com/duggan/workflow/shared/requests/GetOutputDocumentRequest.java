package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetOutputDocumentResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetOutputDocumentRequest extends BaseRequest<GetOutputDocumentResponse> {

	private String outputDocId;
	private boolean isLoadTemplate;

	public GetOutputDocumentRequest() {
	}
	
	public GetOutputDocumentRequest(String outputDocId) {
		this.outputDocId = outputDocId;
	}
	
	public GetOutputDocumentRequest(String outputDocId, boolean isLoadTemplate) {
		this.outputDocId = outputDocId;
		this.isLoadTemplate = isLoadTemplate;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetOutputDocumentResponse();
	}

	public String getOutputDocId() {
		return outputDocId;
	}

	public void setOutputDocId(String outputDocId) {
		this.outputDocId = outputDocId;
	}

	public boolean isLoadTemplate() {
		return isLoadTemplate;
	}

	public void setLoadTemplate(boolean isLoadTemplate) {
		this.isLoadTemplate = isLoadTemplate;
	}

}
