package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.DSConfiguration;

public class GetDSConfigurationsResponse extends BaseResponse {

	private ArrayList<DSConfiguration> configurations;

	public GetDSConfigurationsResponse() {
	}

	public ArrayList<DSConfiguration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(ArrayList<DSConfiguration> configurations) {
		this.configurations = configurations;
	}
}
