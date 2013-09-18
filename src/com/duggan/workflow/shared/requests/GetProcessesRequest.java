package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessesResponse;

public class GetProcessesRequest extends BaseRequest<GetProcessesResponse> {

	public GetProcessesRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetProcessesResponse();
	}
}
