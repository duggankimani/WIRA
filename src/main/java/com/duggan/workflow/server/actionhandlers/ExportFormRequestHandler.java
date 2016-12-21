package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.shared.requests.ExportFormRequest;
import com.duggan.workflow.shared.responses.ExportFormResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class ExportFormRequestHandler extends
		AbstractActionHandler<ExportFormRequest, ExportFormResponse> {

	@Inject
	public ExportFormRequestHandler() {
	}

	@Override
	public void execute(ExportFormRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		String formRefId = action.getFormRefId();
		String xml = FormDaoHelper.exportForm(formRefId);
		
		ExportFormResponse response = (ExportFormResponse)actionResult;
		response.setXml(xml);
	}

	@Override
	public Class<ExportFormRequest> getActionType() {
		return ExportFormRequest.class;
	}
}
