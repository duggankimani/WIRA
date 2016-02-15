package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.requests.GetRequestCountsAction;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetRequestCountsResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * 
 * @author duggan
 *
 *	Get the number of requests per process/ per task / per user
 */
public class GetRequestCountsActionHandler extends
		AbstractActionHandler<GetRequestCountsAction, GetRequestCountsResult> {

	@Inject
	public GetRequestCountsActionHandler() {
	}

	@Override
	public void execute(GetRequestCountsAction action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		GetRequestCountsResult countResult = (GetRequestCountsResult)actionResult;
		JBPMHelper.get().getCounts(action.getProcessId(),action.getUserId(), countResult.getCounts());
	}
	
	@Override
	public Class<GetRequestCountsAction> getActionType() {

		return GetRequestCountsAction.class;
	}
	
}
