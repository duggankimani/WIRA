package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.StartAllProcessesResponse;

public class StartAllProcessesRequest extends
		BaseRequest<StartAllProcessesResponse> {

	public StartAllProcessesRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new StartAllProcessesResponse();
	}
}
