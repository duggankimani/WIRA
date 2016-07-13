package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.server.helper.email.EmailServiceHelper;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.SaveSettingsRequest;
import com.duggan.workflow.shared.responses.SaveSettingsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class SaveSettingsRequestActionHandler extends
		AbstractActionHandler<SaveSettingsRequest, SaveSettingsResponse> {

	@Inject
	public SaveSettingsRequestActionHandler() {
	}

	@Override
	public void execute(SaveSettingsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		SettingsDaoHelper.save(action.getSettings());
		SaveSettingsResponse response = (SaveSettingsResponse)actionResult;
		response.setSettings((ArrayList<Setting>) SettingsDaoHelper.getSettings(null));
		
		//proactively re-initialize email service incase there was a change
		EmailServiceHelper.initProperties(true);
	}
	
	@Override
	public Class<SaveSettingsRequest> getActionType() {
		return SaveSettingsRequest.class;
	}
}
