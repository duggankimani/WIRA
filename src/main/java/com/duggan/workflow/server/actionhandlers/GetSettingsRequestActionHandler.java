package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.GetSettingsRequest;
import com.duggan.workflow.shared.responses.GetSettingsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetSettingsRequestActionHandler extends
		AbstractActionHandler<GetSettingsRequest, GetSettingsResponse> {

	@Inject
	public GetSettingsRequestActionHandler() {
	}

	@Override
	public void execute(GetSettingsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		List<Setting> settings = SettingsDaoHelper.getSettings(action.getSettingNames());
		
		GetSettingsResponse response = (GetSettingsResponse)actionResult;		
		response.setSettings((ArrayList<Setting>) settings);
	}
	
	@Override
	public Class<GetSettingsRequest> getActionType() {
		return GetSettingsRequest.class;
	}
}
