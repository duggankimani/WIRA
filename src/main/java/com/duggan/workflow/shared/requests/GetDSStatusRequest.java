package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.GetDSStatusResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class GetDSStatusRequest extends BaseRequest<GetDSStatusResponse> {

	private String configName;

	@SuppressWarnings("unused")
	public GetDSStatusRequest() {
		// For serialization only
	}

	public GetDSStatusRequest(String configName) {
		this.configName = configName;
	}

	public String getConfigName() {
		return configName;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetDSStatusResponse();
	}
}
