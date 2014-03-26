package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetSettingsResponse;

import java.util.List;

public class GetSettingsRequest extends BaseRequest<GetSettingsResponse> {

	private List<SETTINGNAME> settingNames;

	public GetSettingsRequest() {
	}
	
	public GetSettingsRequest(List<SETTINGNAME> names){
		this.settingNames = names;
	}

	public List<SETTINGNAME> getSettingNames() {
		return settingNames;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetSettingsResponse();
	}
}
