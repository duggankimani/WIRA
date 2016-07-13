package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetDSConfigurationsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetDSConfigurationsRequest extends
		BaseRequest<GetDSConfigurationsResponse> {

	public GetDSConfigurationsRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDSConfigurationsResponse();
	}
}
