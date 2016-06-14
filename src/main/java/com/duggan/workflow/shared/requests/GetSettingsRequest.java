package com.duggan.workflow.shared.requests;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetSettingsResponse;

public class GetSettingsRequest extends BaseRequest<GetSettingsResponse> {

	private ArrayList<SETTINGNAME> settingNames;

	public GetSettingsRequest() {
	}
	
	public GetSettingsRequest(ArrayList<SETTINGNAME> names){
		this.settingNames = names;
	}

	public ArrayList<SETTINGNAME> getSettingNames() {
		return settingNames;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
		return new GetSettingsResponse();
	}
	
	@Override
	public boolean isSecured() {
		return false;
	}
}
