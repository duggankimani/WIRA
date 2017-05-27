package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.DashboardDaoImpl;
import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.requests.GetDashboardDataRequest;
import com.duggan.workflow.shared.responses.GetDashboardDataResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetDashboardDataRequestHandler extends
		AbstractActionHandler<GetDashboardDataRequest, GetDashboardDataResponse> {

	@Inject
	public GetDashboardDataRequestHandler() {
	}

	@Override
	public void execute(GetDashboardDataRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		DashboardDaoImpl dao = DB.getDashboardDao();
		GetDashboardDataResponse response = (GetDashboardDataResponse)actionResult;
		
		String processId = null;
		if(action.getProcessRefId()!=null){
			processId = ProcessDaoHelper.getProcessDef(action.getProcessRefId(), false).getProcessId();
			response.setLongTasks(dao.getLongTasks(processId, action.getStartDate(), action.getEndDate()));
			response.setAging(dao.getProcessAging(processId, action.getStartDate(), action.getEndDate()));
		}
		response.setProcessesSummaries(dao.getProcessesSummary(processId, action.getStartDate(), action.getEndDate()));
		response.setWorkloads(dao.getTasksSummary(processId, action.getStartDate(), action.getEndDate()));
	}

	@Override
	public Class<GetDashboardDataRequest> getActionType() {
		return GetDashboardDataRequest.class;
	}
}
