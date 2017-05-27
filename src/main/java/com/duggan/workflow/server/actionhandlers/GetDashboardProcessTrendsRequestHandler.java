package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.DashboardDaoImpl;
import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.requests.GetDashboardProcessTrendsRequest;
import com.duggan.workflow.shared.responses.GetDashboardProcessTrendsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetDashboardProcessTrendsRequestHandler extends
		AbstractActionHandler<GetDashboardProcessTrendsRequest, GetDashboardProcessTrendsResponse> {

	@Inject
	public GetDashboardProcessTrendsRequestHandler() {
	}

	@Override
	public void execute(GetDashboardProcessTrendsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		DashboardDaoImpl dao = DB.getDashboardDao();
		GetDashboardProcessTrendsResponse response = (GetDashboardProcessTrendsResponse)actionResult;
		
		String processId= null;
		if(action.getProcessRefId()!=null){
			processId = ProcessDaoHelper.getProcessDef(action.getProcessRefId(), false).getProcessId();
		}
		response.setStartTrend(dao.getProcessTrend(processId,action.getStartDate(),action.getEndDate(), action.getPeriod(), 0));
		response.setCompletionTrend(dao.getProcessTrend(processId,action.getStartDate(),action.getEndDate(), action.getPeriod(), 1));
		
	}

	@Override
	public Class<GetDashboardProcessTrendsRequest> getActionType() {
		return GetDashboardProcessTrendsRequest.class;
	}
}
