package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.model.ADTrigger;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.server.mvel.MVELExecutor;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.requests.ExecuteTriggerRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.ExecuteTriggerResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * Trigger Executor
 * @author duggan
 *
 */
public class ExecuteTriggerActionHandler extends
		AbstractActionHandler<ExecuteTriggerRequest, ExecuteTriggerResponse> {

	@Inject
	public ExecuteTriggerActionHandler() {
	}

	@Override
	public void execute(ExecuteTriggerRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Doc doc = action.getDoc();
		boolean canExecute=false;
		
		if(doc instanceof Document){
			if(doc.getOwner()!=null && 
					doc.getOwner().equals(SessionHelper.getCurrentUser()) &&
					((Document)doc).getStatus()==DocStatus.DRAFTED){
				canExecute=true;
			}
		}else{
			HTSummary summary = (HTSummary)doc;
			if(
//					summary.getStatus()==HTStatus.CREATED 
//					|| summary.getStatus()==HTStatus.READY
					 summary.getStatus()==HTStatus.RESERVED
					|| summary.getStatus()==HTStatus.INPROGRESS){
				canExecute=true;
			}
		}
		
		ProcessDaoImpl dao = DB.getProcessDao();
		if(canExecute){
			ADTrigger trigger = dao.getTrigger(action.getTriggerName());
			new MVELExecutor().execute(trigger, doc);
		}
		
		((ExecuteTriggerResponse)actionResult).setDocument(doc);
				
	}
	

	@Override
	public void undo(ExecuteTriggerRequest action, ExecuteTriggerResponse result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<ExecuteTriggerRequest> getActionType() {
		return ExecuteTriggerRequest.class;
	}
}
