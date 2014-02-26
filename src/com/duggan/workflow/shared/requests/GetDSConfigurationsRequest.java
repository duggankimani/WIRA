package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDSConfigurationsResponse;

public class GetDSConfigurationsRequest extends
		BaseRequest<GetDSConfigurationsResponse> {

	public GetDSConfigurationsRequest() {
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDSConfigurationsResponse();
	}
}
