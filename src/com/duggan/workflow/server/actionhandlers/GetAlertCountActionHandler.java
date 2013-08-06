package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.dao.NotificationDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.requests.GetAlertCount;
import com.duggan.workflow.shared.requests.GetAlertCountResult;
import com.duggan.workflow.shared.responses.BaseResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetAlertCountActionHandler extends
		BaseActionHandler<GetAlertCount, GetAlertCountResult> {

	@Inject
	public GetAlertCountActionHandler() {
	}

	@Override
	public void execute(GetAlertCount action,
			BaseResult actionResult, ExecutionContext execContext)
			throws ActionException {

		GetAlertCountResult countResult = (GetAlertCountResult)actionResult;
		JBPMHelper.get().getCount(SessionHelper.getCurrentUser().getId(), countResult.getCounts());
		DocumentDaoHelper.getCounts(countResult.getCounts());
		NotificationDaoHelper.getCounts(countResult.getCounts());
	}
	
	@Override
	public Class<GetAlertCount> getActionType() {

		return GetAlertCount.class;
	}
	
}
