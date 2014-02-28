package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.requests.GetItemRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetItemResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetItemActionHandler extends
		BaseActionHandler<GetItemRequest, GetItemResult> {

	@Inject
	public GetItemActionHandler() {
	}

	@Override
	public void execute(GetItemRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {

		Doc doc=null;
		if(action.isTask()){
			HTSummary summary = JBPMHelper.get().getSummary(action.getItemId());
		}else{
			doc = DocumentDaoHelper.getDocument(action.getItemId());
		}
		
	}

	@Override
	public void undo(GetItemRequest action, GetItemResult result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<GetItemRequest> getActionType() {
		return GetItemRequest.class;
	}
}
