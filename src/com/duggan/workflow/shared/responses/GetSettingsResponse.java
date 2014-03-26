package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.settings.Setting;

public class GetSettingsResponse extends BaseResponse {

	private List<Setting> settings;

	public GetSettingsResponse() {
	}

	public List<Setting> getSettings() {
		return settings;
	}

	public void setSettings(List<Setting> settings) {
		this.settings = settings;
	}
}
