package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetUsersResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetUsersRequest extends BaseRequest<GetUsersResponse> {

	private String searchTerm;

	public GetUsersRequest() {
	}
	
	public GetUsersRequest(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetUsersResponse();
	}

	public String getSearchTerm() {
		return searchTerm;
	}

}
