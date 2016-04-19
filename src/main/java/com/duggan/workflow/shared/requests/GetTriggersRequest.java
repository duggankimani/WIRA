package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTriggersResponse;

public class GetTriggersRequest extends BaseRequest<GetTriggersResponse> {

	private String processRefId;
	private String searchTerm;

	public GetTriggersRequest() {
	}
	
	public GetTriggersRequest(String processRefId) {
		this.processRefId = processRefId;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetTriggersResponse();
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
