package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.shared.requests.SaveSettingsRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveSettingsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveSettingsRequestActionHandler extends
		BaseActionHandler<SaveSettingsRequest, SaveSettingsResponse> {

	@Inject
	public SaveSettingsRequestActionHandler() {
	}

	@Override
	public void execute(SaveSettingsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		SettingsDaoHelper.save(action.getSettings());
		SaveSettingsResponse response = (SaveSettingsResponse)actionResult;
		response.setSettings(SettingsDaoHelper.getSettings(null));
	}
	
	@Override
	public Class<SaveSettingsRequest> getActionType() {
		return SaveSettingsRequest.class;
	}
}