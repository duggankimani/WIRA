package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetProcessCategoriesResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetProcessCategoriesRequest extends BaseRequest<GetProcessCategoriesResponse> {

	public GetProcessCategoriesRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new GetProcessCategoriesResponse();
	}
}
