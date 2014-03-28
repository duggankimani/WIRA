package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.GetSettingsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetSettingsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetSettingsRequestActionHandler extends
		BaseActionHandler<GetSettingsRequest, GetSettingsResponse> {

	@Inject
	public GetSettingsRequestActionHandler() {
	}

	@Override
	public void execute(GetSettingsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		List<Setting> settings = SettingsDaoHelper.getSettings(action.getSettingNames());
		
		GetSettingsResponse response = (GetSettingsResponse)actionResult;		
		response.setSettings(settings);
	}
	
	@Override
	public Class<GetSettingsRequest> getActionType() {
		return GetSettingsRequest.class;
	}
}
