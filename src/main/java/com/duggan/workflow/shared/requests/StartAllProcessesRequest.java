package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.StartAllProcessesResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class StartAllProcessesRequest extends
		BaseRequest<StartAllProcessesResponse> {

	public StartAllProcessesRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new StartAllProcessesResponse();
	}
}
