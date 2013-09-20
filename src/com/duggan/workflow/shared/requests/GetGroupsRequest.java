package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetGroupsResponse;

public class GetGroupsRequest extends BaseRequest<GetGroupsResponse> {

	public GetGroupsRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetGroupsResponse();
	}
}
