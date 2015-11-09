package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.NotificationDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.requests.GetAlertCount;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetAlertCountResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetAlertCountActionHandler extends
		AbstractActionHandler<GetAlertCount, GetAlertCountResult> {

	@Inject
	public GetAlertCountActionHandler() {
	}

	@Override
	public void execute(GetAlertCount action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {

		GetAlertCountResult countResult = (GetAlertCountResult)actionResult;
		JBPMHelper.get().getCount(SessionHelper.getCurrentUser().getUserId(), countResult.getCounts());
		NotificationDaoHelper.getCounts(countResult.getCounts());
	}
	
	@Override
	public Class<GetAlertCount> getActionType() {

		return GetAlertCount.class;
	}
	
}
