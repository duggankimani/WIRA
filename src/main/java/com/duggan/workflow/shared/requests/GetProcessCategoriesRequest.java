package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetProcessCategoriesResponse;

public class GetProcessCategoriesRequest extends BaseRequest<GetProcessCategoriesResponse> {

	public GetProcessCategoriesRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetProcessCategoriesResponse();
	}
}
