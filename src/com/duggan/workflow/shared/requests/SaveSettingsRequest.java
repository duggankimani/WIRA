package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveSettingsResponse;

import java.util.List;

public class SaveSettingsRequest extends BaseRequest<SaveSettingsResponse> {

	private List<Setting> settings;

	public SaveSettingsRequest() {
	}

	public SaveSettingsRequest(List<Setting> settings) {
		this.settings = settings;
	}

	public List<Setting> getSettings() {
		return settings;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new SaveSettingsResponse();
	}
}
