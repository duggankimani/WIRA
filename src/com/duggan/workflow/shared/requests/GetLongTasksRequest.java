package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetLongTasksResponse;

public class GetLongTasksRequest extends BaseRequest<GetLongTasksResponse> {

	public GetLongTasksRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetLongTasksResponse();
	}
}
