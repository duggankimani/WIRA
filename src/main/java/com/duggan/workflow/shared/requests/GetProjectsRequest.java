package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetProjectsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetProjectsRequest extends BaseRequest<GetProjectsResponse> {

	public GetProjectsRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetProjectsResponse();
	}
}
