package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.SaveProcessRequest;
import com.duggan.workflow.shared.responses.SaveProcessResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class SaveProcessRequestActionHandler extends
		AbstractActionHandler<SaveProcessRequest, SaveProcessResponse> {

	@Inject
	public SaveProcessRequestActionHandler() {
	}
	
	@Override
	public void execute(SaveProcessRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		ProcessDef def = action.getProcessDef();
		
		def = ProcessDaoHelper.save(def);
		
		SaveProcessResponse response = (SaveProcessResponse)actionResult;
		response.setProcessDef(def);
	}
	
	@Override
	public Class<SaveProcessRequest> getActionType() {
		return SaveProcessRequest.class;
	}
}
