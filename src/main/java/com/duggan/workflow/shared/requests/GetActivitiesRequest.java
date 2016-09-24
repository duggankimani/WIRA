package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetActivitiesResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetActivitiesRequest extends
		BaseRequest<GetActivitiesResponse> {

	private String docRefId;
	private String processRefId;
	private boolean categorized=true;//parent child relationship set
	
	public GetActivitiesRequest() {
	}
	
	public GetActivitiesRequest(String docRefId) {
		this.docRefId = docRefId;
	}
	
	public String getDocRefId() {
		return docRefId;
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

	public String getProcessRefId() {
		return processRefId;
	}

	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}

}
