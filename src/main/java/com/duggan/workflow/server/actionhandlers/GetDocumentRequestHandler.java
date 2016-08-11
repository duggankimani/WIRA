package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.requests.GetDocumentRequest;
import com.duggan.workflow.shared.responses.GetDocumentResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

/**
 * 
 * @author duggan
 *
 */
public class GetDocumentRequestHandler extends
		AbstractActionHandler<GetDocumentRequest, GetDocumentResult> {

	@Inject
	public GetDocumentRequestHandler() {
	}

	@Override
	public void execute(GetDocumentRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Doc doc = null;
		if(action.getTaskId()==null){
			doc = DocumentDaoHelper.getDocJson(action.getDocRefId());
			//doc = DocumentDaoHelper.getDocument(action.getDocRefId());
		}else{
			doc = JBPMHelper.get().getTask(action.getTaskId());
		}
		
		
		GetDocumentResult result = (GetDocumentResult)actionResult;
		
		result.setDoc(doc);		
		
	}

	@Override
	public void undo(GetDocumentRequest action, GetDocumentResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetDocumentRequest> getActionType() {
		return GetDocumentRequest.class;
	}
}
