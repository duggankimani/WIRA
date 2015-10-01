package com.duggan.workflow.server.actionhandlers;

import java.util.Collection;

import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.server.dao.model.ADTaskStepTrigger;
import com.duggan.workflow.server.dao.model.TaskStepModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.server.mvel.MVELExecutor;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.TriggerType;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.requests.ExecuteTriggersRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.ExecuteTriggersResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * Trigger Executor
 * @author duggan
 *
 */
public class ExecuteTriggersActionHandler extends
		BaseActionHandler<ExecuteTriggersRequest, ExecuteTriggersResponse> {

	@Inject
	public ExecuteTriggersActionHandler() {
	}

	@Override
	public void execute(ExecuteTriggersRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Doc doc = action.getDoc();
		
		boolean canExecute=false;
		
		if(doc instanceof Document){
			if(doc.getOwner()!=null && 
					doc.getOwner().equals(SessionHelper.getCurrentUser()) &&
					((Document)doc).getStatus()==DocStatus.DRAFTED){
				canExecute=true;
				
				//Save this doc -- ADDED BY DUGGAN 27/09/2015  -
				//Draft Documents should be saved on navigation
				doc = DocumentDaoHelper.save((Document)doc);
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
			
			//After Step Triggers
			Collection<ADTaskStepTrigger> adTriggers= dao.getTaskStepTriggers(action.getPreviousStepId(),
					TriggerType.AFTERSTEP);
			for(ADTaskStepTrigger stepTrigger: adTriggers){
				new MVELExecutor().execute(stepTrigger.getTrigger(), doc);
			}
			
			//Before Next Step Triggers
			if(action.getNextStepId()!=null){
				adTriggers= dao.getTaskStepTriggers(action.getNextStepId(), TriggerType.BEFORESTEP);
				for(ADTaskStepTrigger stepTrigger: adTriggers){
					new MVELExecutor().execute(stepTrigger.getTrigger(), doc);
				}
			}
		}
		
		if(canExecute && action.getNextStepId()!=null){
			//Next step is an Output Document
			TaskStepModel model = dao.getById(TaskStepModel.class, action.getNextStepId());
			if(model.getDoc()!=null){
				Value value = OutputDocumentDaoHelper.generateDoc(model.getDoc(), doc);
				doc.setValue(value.getKey(), value);
			}
		}
		
		((ExecuteTriggersResponse)actionResult).setDocument(doc);
				
	}
	

	@Override
	public void undo(ExecuteTriggersRequest action, ExecuteTriggersResponse result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<ExecuteTriggersRequest> getActionType() {
		return ExecuteTriggersRequest.class;
	}
}
