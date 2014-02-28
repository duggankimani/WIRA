package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;

import java.lang.Long;

public class DeleteDSConfigurationEvent extends
		BaseRequest<BaseResponse> {

	private Long configurationId;

	@SuppressWarnings("unused")
	private DeleteDSConfigurationEvent() {
		// For serialization only
	}

	public DeleteDSConfigurationEvent(Long configurationId) {
		this.configurationId = configurationId;
	}

	public Long getConfigurationId() {
		return configurationId;
	}
}
