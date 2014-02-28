package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.exceptions.IllegalApprovalRequestException;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.BaseResponse;
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
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		Document doc= action.getDocument();
		doc.setStatus(DocStatus.INPROGRESS);
		doc = DocumentDaoHelper.save(doc);
		
		if(doc.getProcessInstanceId()!=null){
			throw new IllegalApprovalRequestException(doc);
		}
		String userId = action.getUsername();
		if(userId==null)
			userId = SessionHelper.getCurrentUser().getUserId();
		
		JBPMHelper.get().createApprovalRequest(userId, doc);
		
		ApprovalRequestResult result = (ApprovalRequestResult)actionResult;
		result.setSuccessfulSubmit(true);
		result.setDocument(doc);
				
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
