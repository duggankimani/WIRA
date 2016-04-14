package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetGroupsResponse;

public class GetGroupsRequest extends BaseRequest<GetGroupsResponse> {

	private String groupName;

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

}
