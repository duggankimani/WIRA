package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetFormsResponse;

public class GetFormsRequest extends BaseRequest<GetFormsResponse> {

	public GetFormsRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetFormsResponse();
	}
}
