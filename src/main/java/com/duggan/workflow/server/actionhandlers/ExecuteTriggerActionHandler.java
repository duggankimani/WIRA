package com.duggan.workflow.server.actionhandlers;

import java.util.Collection;

import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.model.ADTaskStepTrigger;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.mvel.MVELExecutor;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.TriggerType;
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
public class ExecuteTriggerActionHandler extends
		BaseActionHandler<ExecuteTriggersRequest, ExecuteTriggersResponse> {

	@Inject
	public ExecuteTriggerActionHandler() {
	}

	@Override
	public void execute(ExecuteTriggersRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Doc doc = action.getDoc();
		
		ProcessDaoImpl dao = DB.getProcessDao();
		
		//After Step Triggers
		Collection<ADTaskStepTrigger> adTriggers= dao.getTaskStepTriggers(action.getPreviousStepId(), TriggerType.AFTERSTEP);
		for(ADTaskStepTrigger stepTrigger: adTriggers){
			new MVELExecutor().execute(stepTrigger.getTrigger(), doc);
		}
		
		//Before Next Step Triggers
		adTriggers= dao.getTaskStepTriggers(action.getNextStepId(), TriggerType.BEFORESTEP);
		for(ADTaskStepTrigger stepTrigger: adTriggers){
			new MVELExecutor().execute(stepTrigger.getTrigger(), doc);
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