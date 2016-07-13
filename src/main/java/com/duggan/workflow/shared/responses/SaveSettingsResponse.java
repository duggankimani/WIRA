package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.settings.Setting;
import com.wira.commons.shared.response.BaseResponse;

public class SaveSettingsResponse extends BaseResponse {

	private ArrayList<Setting> settings;

	public SaveSettingsResponse() {
	}

	public ArrayList<Setting> getSettings() {
		return settings;
	}

	public void setSettings(ArrayList<Setting> settings) {
		this.settings = settings;
	}

}
