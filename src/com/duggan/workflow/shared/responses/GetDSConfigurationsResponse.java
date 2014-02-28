package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.DSConfiguration;
import java.util.List;

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
