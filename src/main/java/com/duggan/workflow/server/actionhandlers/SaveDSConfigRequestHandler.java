package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.DSConfigHelper;
import com.duggan.workflow.shared.model.DSConfiguration;
import com.duggan.workflow.shared.requests.SaveDSConfigRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveDSConfigResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveDSConfigRequestHandler extends
		AbstractActionHandler<SaveDSConfigRequest, SaveDSConfigResponse> {

	@Inject
	public SaveDSConfigRequestHandler() {
	}

	@Override
	public void execute(SaveDSConfigRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		DSConfiguration configToSave = action.getConfiguration();
		
		DSConfiguration saved = DSConfigHelper.save(configToSave);
		
		SaveDSConfigResponse response = (SaveDSConfigResponse)actionResult;
		
		response.setConfiguration(saved);
	}

	@Override
	public Class<SaveDSConfigRequest> getActionType() {
		return SaveDSConfigRequest.class;
	}
}
