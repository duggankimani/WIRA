package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetLongTasksResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetLongTasksRequest extends BaseRequest<GetLongTasksResponse> {

	public GetLongTasksRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetLongTasksResponse();
	}
}
