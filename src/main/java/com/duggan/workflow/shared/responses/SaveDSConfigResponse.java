package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.DSConfiguration;
import com.wira.commons.shared.response.BaseResponse;

public class SaveDSConfigResponse extends BaseResponse {

	private DSConfiguration configuration;

	public SaveDSConfigResponse() {
	}

	public DSConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(DSConfiguration configuration) {
		this.configuration = configuration;
	}
}
