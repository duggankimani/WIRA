package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetGroupsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetGroupsRequest extends BaseRequest<GetGroupsResponse> {

	private String groupName;
	private String searchTerm;

	public GetGroupsRequest() {
	}
	
	public GetGroupsRequest(String groupName) {
		this.groupName = groupName;
		
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetGroupsResponse();
	}

	public String getGroupName() {
		return groupName;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

}
