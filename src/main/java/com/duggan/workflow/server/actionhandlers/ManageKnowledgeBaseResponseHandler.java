package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.ManageProcessAction;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.ManageKnowledgeBaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.ManageKnowledgeBaseResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class ManageKnowledgeBaseResponseHandler
		extends
		AbstractActionHandler<ManageKnowledgeBaseRequest, ManageKnowledgeBaseResponse> {

	@Inject
	public ManageKnowledgeBaseResponseHandler() {
	}

	@Override
	public void execute(ManageKnowledgeBaseRequest request,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		ManageProcessAction action = request.getAction();
		
		switch (action) {
		case ACTIVATE:
			ProcessMigrationHelper.start(request.getProcessDefId(), request.isForce());
			break;
		case DEACTIVATE:
			ProcessMigrationHelper.stop(request.getProcessDefId());
			break;
		}
		
		ProcessDef def = ProcessDaoHelper.getProcessDef(request.getProcessDefId());
		
		ManageKnowledgeBaseResponse response = (ManageKnowledgeBaseResponse)actionResult;
		response.setProcessDef(def);
	}

	@Override
	public Class<ManageKnowledgeBaseRequest> getActionType() {
		return ManageKnowledgeBaseRequest.class;
	}
}
