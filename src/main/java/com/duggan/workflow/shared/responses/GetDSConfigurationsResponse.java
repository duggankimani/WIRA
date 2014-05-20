package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.DSConfiguration;

public class GetDSConfigurationsResponse extends BaseResponse {

	private List<DSConfiguration> configurations;

	public GetDSConfigurationsResponse() {
	}

	public List<DSConfiguration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<DSConfiguration> configurations) {
		this.configurations = configurations;
	}
}
