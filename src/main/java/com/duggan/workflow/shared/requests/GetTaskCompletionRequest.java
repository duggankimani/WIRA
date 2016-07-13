package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetTaskCompletionResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetTaskCompletionRequest extends
		BaseRequest<GetTaskCompletionResponse> {

	public GetTaskCompletionRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		
		return new GetTaskCompletionResponse();
	}
}
