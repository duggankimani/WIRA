package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.dao.FormDaoHelper;
import com.duggan.workflow.shared.requests.ExportFormRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.ExportFormResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class ExportFormRequestHandler extends
		BaseActionHandler<ExportFormRequest, ExportFormResponse> {

	@Inject
	public ExportFormRequestHandler() {
	}

	@Override
	public void execute(ExportFormRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		Long formId = action.getFormId();
		String xml = FormDaoHelper.exportForm(formId);
		
		ExportFormResponse response = (ExportFormResponse)actionResult;
		response.setXml(xml);
	}

	@Override
	public Class<ExportFormRequest> getActionType() {
		return ExportFormRequest.class;
	}
}
