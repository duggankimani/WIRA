package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.DSConfiguration;
import com.wira.commons.shared.response.BaseResponse;

/**
 * 
 * @author duggan
 *
 */
public class GetDSStatusResponse extends BaseResponse {

	private ArrayList<DSConfiguration> configs = new ArrayList<DSConfiguration>();

	public GetDSStatusResponse() {
	}

	public ArrayList<DSConfiguration> getConfigs() {
		return configs;
	}

	public void setConfigs(ArrayList<DSConfiguration> configs) {
		this.configs = configs;
	}

	public void addConfig(DSConfiguration config) {
		configs.add(config);
	}

}
