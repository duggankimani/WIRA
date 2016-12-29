package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.servlets.upload.ImportLogger;
import com.duggan.workflow.shared.requests.GetProcessImportStatus;
import com.duggan.workflow.shared.responses.GetProcessImportStatusResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetProcessImportStatusActionHandler extends
		AbstractActionHandler<GetProcessImportStatus, GetProcessImportStatusResponse> {

	@Inject
	public GetProcessImportStatusActionHandler() {
	}
	
	@Override
	public void execute(GetProcessImportStatus action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetProcessImportStatusResponse aResponse = (GetProcessImportStatusResponse)actionResult;
		aResponse.setStatus(ImportLogger.get());
	}
	
	@Override
	public Class<GetProcessImportStatus> getActionType() {
		return GetProcessImportStatus.class;
	}
}
