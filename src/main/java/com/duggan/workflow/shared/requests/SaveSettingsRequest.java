package com.duggan.workflow.shared.requests;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.responses.SaveSettingsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class SaveSettingsRequest extends BaseRequest<SaveSettingsResponse> {

	private ArrayList<Setting> settings;

	public SaveSettingsRequest() {
	}

	public SaveSettingsRequest(ArrayList<Setting> settings) {
		this.settings = settings;
	}

	public ArrayList<Setting> getSettings() {
		return settings;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveSettingsResponse();
	}
}
