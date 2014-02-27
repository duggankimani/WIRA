package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetActivitiesResponse;

import java.lang.Long;

public class GetActivitiesRequest extends
		BaseRequest<GetActivitiesResponse> {

	private Long documentId;
	private boolean categorized=true;//parent child relationship set
	
	@SuppressWarnings("unused")
	private GetActivitiesRequest() {
	}

	public GetActivitiesRequest(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocumentId() {
		return documentId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetActivitiesResponse();
	}

	public boolean isCategorized() {
		return categorized;
	}

	public void setCategorized(boolean categorized) {
		this.categorized = categorized;
	}
}
