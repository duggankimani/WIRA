package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.DSConfiguration;
import com.duggan.workflow.shared.responses.SaveDSConfigResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class SaveDSConfigRequest extends BaseRequest<SaveDSConfigResponse> {

	private DSConfiguration configuration;

	@SuppressWarnings("unused")
	private SaveDSConfigRequest() {
		// For serialization only
	}

	public SaveDSConfigRequest(DSConfiguration configuration) {
		this.configuration = configuration;
	}

	public DSConfiguration getConfiguration() {
		return configuration;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveDSConfigResponse();
	}
}
