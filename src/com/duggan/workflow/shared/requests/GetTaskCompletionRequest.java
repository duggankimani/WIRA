package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetTaskCompletionResponse;

public class GetTaskCompletionRequest extends
		BaseRequest<GetTaskCompletionResponse> {

	public GetTaskCompletionRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetTaskCompletionResponse();
	}
}
