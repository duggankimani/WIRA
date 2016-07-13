package com.duggan.workflow.shared.requests;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.responses.GetSettingsResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

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
