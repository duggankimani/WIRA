package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.SaveProcessRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveProcessResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveProcessRequestActionHandler extends
		BaseActionHandler<SaveProcessRequest, SaveProcessResponse> {

	@Inject
	public SaveProcessRequestActionHandler() {
	}
	
	@Override
	public void execute(SaveProcessRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		ProcessDef def = action.getProcessDef();
		
		def = ProcessDefHelper.save(def);
		
		SaveProcessResponse response = (SaveProcessResponse)actionResult;
		response.setProcessDef(def);
	}
	
	@Override
	public Class<SaveProcessRequest> getActionType() {
		return SaveProcessRequest.class;
	}
}
