package com.duggan.workflow.server.actionhandlers;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.duggan.workflow.server.dao.helper.DSConfigHelper;
import com.duggan.workflow.shared.requests.DeleteDSConfigurationEvent;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteDSConfigurationEventActionHandler
		extends
		BaseActionHandler<DeleteDSConfigurationEvent, BaseResponse> {

	@Inject
	public DeleteDSConfigurationEventActionHandler() {
	}

	@Override
	public void execute(DeleteDSConfigurationEvent action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		DSConfigHelper.delete(action.getConfigurationId());
	}

	@Override
	public Class<DeleteDSConfigurationEvent> getActionType() {
		return DeleteDSConfigurationEvent.class;
	}
}
