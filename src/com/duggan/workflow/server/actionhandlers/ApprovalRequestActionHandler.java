package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.BaseResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class ApprovalRequestActionHandler extends
		BaseActionHandler<ApprovalRequest, ApprovalRequestResult> {

	@Inject
	public ApprovalRequestActionHandler() {
	}

	@Override
	public void execute(ApprovalRequest action,
			BaseResult actionResult, ExecutionContext execContext)
			throws ActionException {
		Document doc= action.getDocument();
		doc.setStatus(DocStatus.INPROGRESS);
		doc = DocumentDaoHelper.save(doc);
		
		JBPMHelper.get().createApprovalRequest(doc);
		
		ApprovalRequestResult result = (ApprovalRequestResult)actionResult;
		result.setSuccessfulSubmit(true);
		
	}
	

	@Override
	public void undo(ApprovalRequest action, ApprovalRequestResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<ApprovalRequest> getActionType() {
		return ApprovalRequest.class;
	}
}
